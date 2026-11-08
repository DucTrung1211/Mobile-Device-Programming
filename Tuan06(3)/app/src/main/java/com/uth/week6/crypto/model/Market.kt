package com.uth.week6.crypto.model

import java.math.BigDecimal

enum class Coin(val title: String) {
    ETH("Ethereum"),
    BTC("Bitcoin"),
    LTC("Litecoin"),
    XMR("Monero")
}

enum class ChangePeriod(val label: String) {
    UTC_DAY("từ mở cửa ngày UTC"),
    HOURS_24("24 giờ")
}

data class Quote(
    val coin: Coin,
    val source: String,
    val price: BigDecimal,
    val changePercent: Double,
    val period: ChangePeriod,
    val receivedAt: Long
)

data class SourceState(
    val name: String,
    val quotes: List<Quote> = emptyList(),
    val error: String? = null,
    val retryAt: Long = 0,
    val failures: Int = 0,
    val statusCode: Int? = null
)

data class Sample(val receivedAt: Long, val price: BigDecimal)

data class MarketState(
    val sources: List<SourceState> = emptyList(),
    val histories: Map<String, List<Sample>> = emptyMap(),
    val favorites: Set<String> = setOf("ETH", "BTC"),
    val loading: Boolean = true,
    val polling: Boolean = false,
    val now: Long = 0,
    val selected: Coin? = null
) {
    fun sourceQuote(coin: Coin): Pair<Quote, Boolean>? {
        val candidates = sources.flatMap { source ->
            source.quotes.filter { it.coin == coin }.map { it to (source.error != null || now - it.receivedAt > 15_000) }
        }
        return candidates.firstOrNull { !it.second } ?: candidates.maxByOrNull { it.first.receivedAt }
    }
    fun samples(quote: Quote) = histories[quote.source + ":" + quote.coin.name].orEmpty()
}
