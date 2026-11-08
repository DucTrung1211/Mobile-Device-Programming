package com.uth.week6.crypto.data

import com.google.gson.JsonObject
import com.uth.week6.crypto.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.IOException
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

interface PublicMarketApi {
    @GET suspend fun fetch(@Url url: String): Response<JsonObject>
}

data class MarketSource(val name: String, val url: String)

val publicSources = listOf(
    MarketSource("Kraken", "https://api.kraken.com/0/public/Ticker?pair=XBTUSD,ETHUSD,LTCUSD,XMRUSD"),
    MarketSource("Coinbase", "https://api.exchange.coinbase.com/products/BTC-USD/stats"),
    MarketSource("Gemini", "https://api.gemini.com/v2/ticker/ethusd"),
    MarketSource("Bitstamp", "https://www.bitstamp.net/api/v2/ticker/ltcusd/")
)

fun marketApi(): PublicMarketApi {
    val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .callTimeout(12, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().header("Accept", "application/json")
                .header("User-Agent", "UTH-Crypto-Lesson/1.0").build())
        }
        .build()
    return Retrofit.Builder().baseUrl("https://api.kraken.com/").client(client)
        .addConverterFactory(GsonConverterFactory.create()).build().create(PublicMarketApi::class.java)
}

fun retryAfterMillis(value: String?, now: Long): Long {
    if (value == null) return 0
    value.trim().toLongOrNull()?.let { seconds ->
        return seconds.coerceAtLeast(0).coerceAtMost(Long.MAX_VALUE / 1000) * 1000
    }
    return try {
        (ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant().toEpochMilli() - now).coerceAtLeast(0)
    } catch (_: Exception) {
        0
    }
}

class SourceFailure(val code: Int?, val waitMillis: Long, message: String) : IOException(message)

class CryptoRepository(
    private val api: PublicMarketApi = marketApi(),
    private val sources: List<MarketSource> = publicSources,
    private val clock: () -> Long = System::currentTimeMillis
) {
    private var previous = emptyMap<String, SourceState>()
    private val mutex = Mutex()

    suspend fun poll(): List<SourceState> = mutex.withLock {
        // Bốn request độc lập chạy đồng thời. Một nguồn lỗi không hủy nguồn khác.
        val snapshots = supervisorScope {
            sources.map { source -> async { fetch(source, previous[source.name]) } }.awaitAll()
        }
        previous = snapshots.associateBy { it.name }
        snapshots
    }

    private suspend fun fetch(source: MarketSource, old: SourceState?): SourceState {
        val now = clock()
        if (old != null && now < old.retryAt) return old
        return try {
            val response = api.fetch(source.url)
            if (!response.isSuccessful) {
                response.errorBody()?.close()
                val retry = retryAfterMillis(response.headers()["Retry-After"], clock())
                throw SourceFailure(response.code(), retry,
                    if (response.code() == 429) "HTTP 429: giới hạn tần suất" else "HTTP " + response.code())
            }
            val body = response.body() ?: throw IOException("Phản hồi trống")
            val quotes = parseQuotes(source.name, body, clock())
            require(quotes.isNotEmpty()) { "Không có giá hợp lệ" }
            SourceState(source.name, quotes, statusCode = response.code())
        } catch (error: Exception) {
            if (error is CancellationException) throw error
            val failures = ((old?.failures ?: 0) + 1).coerceAtMost(10)
            val delay = (3000L * (1L shl (failures - 1))).coerceAtMost(60_000)
            val minimum = if (error is SourceFailure && error.code == 429) 30_000L else 0L
            val wait = maxOf(delay, minimum, (error as? SourceFailure)?.waitMillis ?: 0)
            SourceState(source.name, old?.quotes.orEmpty(),
                when (error) {
                    is SourceFailure -> error.message
                    is java.net.SocketTimeoutException -> "Kết nối quá thời gian"
                    is java.net.UnknownHostException -> "Không kết nối được máy chủ"
                    else -> "Lỗi dữ liệu hoặc kết nối"
                },
                retryAt = clock().let { if (Long.MAX_VALUE - it < wait) Long.MAX_VALUE else it + wait },
                failures = failures, statusCode = (error as? SourceFailure)?.code)
        }
    }
}

fun parseQuotes(source: String, body: JsonObject, receivedAt: Long): List<Quote> {
    fun quote(coin: Coin, data: JsonObject, last: String, open: String, period: ChangePeriod, explicitPercent: String? = null): Quote {
        val price = data.get(last)?.asBigDecimal ?: error("Thiếu giá")
        val opening = data.get(open)?.asBigDecimal ?: error("Thiếu giá mở cửa")
        require(price > BigDecimal.ZERO && opening > BigDecimal.ZERO)
        val percent = explicitPercent?.let { data.get(it)?.asDouble }
            ?: price.subtract(opening).divide(opening, 12, java.math.RoundingMode.HALF_UP).multiply(BigDecimal(100)).toDouble()
        require(percent.isFinite())
        return Quote(coin, source, price, percent, period, receivedAt)
    }
    return when (source) {
        "Kraken" -> {
            val errors = body.getAsJsonArray("error")
            if (errors != null && errors.size() > 0) {
                val rateLimited = errors.any { it.asString.contains("rate", true) }
                throw SourceFailure(if (rateLimited) 429 else null, 0, "Kraken trả lỗi thị trường")
            }
            val result = body.getAsJsonObject("result") ?: error("Thiếu result")
            listOf(Coin.ETH to "XETHZUSD", Coin.BTC to "XXBTZUSD", Coin.LTC to "XLTCZUSD", Coin.XMR to "XXMRZUSD").map { (coin, key) ->
                val value = result.getAsJsonObject(key) ?: error("Thiếu " + key)
                val adapted = value.deepCopy().apply { add("last", value.getAsJsonArray("c")[0]) }
                quote(coin, adapted, "last", "o", ChangePeriod.UTC_DAY)
            }
        }
        "Coinbase" -> listOf(quote(Coin.BTC, body, "last", "open", ChangePeriod.HOURS_24))
        "Gemini" -> listOf(quote(Coin.ETH, body, "close", "open", ChangePeriod.HOURS_24))
        "Bitstamp" -> listOf(quote(Coin.LTC, body, "last", "open_24", ChangePeriod.HOURS_24, "percent_change_24"))
        else -> error("Nguồn không được hỗ trợ")
    }
}
