package com.uth.week6.crypto

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.uth.week6.crypto.data.*
import com.uth.week6.crypto.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import okhttp3.Headers
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import retrofit2.Response

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class CryptoRepositoryTest {
    private fun json(text: String) = JsonParser.parseString(text).asJsonObject
    private val valid = """{"open":"100","last":"110","close":"110","open_24":"100","percent_change_24":"10"}"""

    @Test fun fourSourcesRunConcurrentlyAndOneFailureIsIsolated() = runTest {
        var active = 0
        var peak = 0
        val api = object : PublicMarketApi {
            override suspend fun fetch(url: String): Response<JsonObject> {
                active++
                peak = maxOf(peak, active)
                delay(100)
                active--
                return if ("kraken" in url) Response.error(503, "{}".toResponseBody())
                    else Response.success(json(valid))
            }
        }
        val snapshots = CryptoRepository(api).poll()
        assertEquals(4, peak)
        assertEquals(3, snapshots.count { it.error == null })
        assertNotNull(snapshots.first { it.name == "Kraken" }.error)
        assertEquals(100, testScheduler.currentTime)
    }

    @Test fun failedSourceRetainsLastGoodPriceAndBacksOff() = runTest {
        var now = 1000L
        var fail = false
        var calls = 0
        val api = object : PublicMarketApi {
            override suspend fun fetch(url: String): Response<JsonObject> {
                calls++
                return if (fail) Response.error(500, "{}".toResponseBody()) else Response.success(json(valid))
            }
        }
        val repository = CryptoRepository(api, listOf(publicSources[1])) { now }
        val good = repository.poll().single().quotes.single()
        fail = true
        now += 3000
        val bad = repository.poll().single()
        assertEquals(good, bad.quotes.single())
        assertNotNull(bad.error)
        repository.poll()
        assertEquals(2, calls)
        now = bad.retryAt
        repository.poll()
        assertEquals(3, calls)
    }

    @Test fun retryAfterSecondsAndHttpDateAreRespected() {
        assertEquals(60_000L, retryAfterMillis("60", 0))
        assertEquals(60_000L, retryAfterMillis("Thu, 01 Jan 1970 00:01:00 GMT", 0))
        assertEquals(0L, retryAfterMillis("invalid", 0))
    }

    @Test fun http429PreservesServerRetryAfter() = runTest {
        var calls = 0
        var now = 1000L
        val api = object : PublicMarketApi {
            override suspend fun fetch(url: String): Response<JsonObject> {
                calls++
                return Response.error("{}".toResponseBody(), okhttp3.Response.Builder()
                    .request(okhttp3.Request.Builder().url("https://example.com").build())
                    .protocol(okhttp3.Protocol.HTTP_1_1).code(429).message("Too Many Requests")
                    .headers(Headers.headersOf("Retry-After", "120")).build())
            }
        }
        val repository = CryptoRepository(api, listOf(publicSources[1])) { now }
        val result = repository.poll().single()
        assertEquals(121_000L, result.retryAt)
        now += 60_000
        repository.poll()
        assertEquals(1, calls)
    }

    @Test fun invalidPriceDoesNotBecomeZero() = runTest {
        val api = object : PublicMarketApi {
            override suspend fun fetch(url: String) = Response.success(json("""{"open":"100","last":"bad"}"""))
        }
        val result = CryptoRepository(api, listOf(publicSources[1])).poll().single()
        assertTrue(result.quotes.isEmpty())
        assertNotNull(result.error)
    }

    @Test fun cancellationIsNotSwallowed() = runTest {
        val api = object : PublicMarketApi {
            override suspend fun fetch(url: String): Response<JsonObject> = throw CancellationException("background")
        }
        try {
            CryptoRepository(api, listOf(publicSources[1])).poll()
            fail("Cancellation must propagate")
        } catch (_: CancellationException) { }
    }

    @Test fun coinbasePercentageHas24HourMeaning() {
        val quote = parseQuotes("Coinbase", json(valid), 42).single()
        assertEquals(10.0, quote.changePercent, 0.0001)
        assertEquals(ChangePeriod.HOURS_24, quote.period)
        assertEquals(42, quote.receivedAt)
    }

    @Test fun livePublicEndpoints() = runTest {
        assumeTrue(System.getProperty("liveApi") == "true")
        val snapshots = CryptoRepository().poll()
        snapshots.forEach { println(it.name + ": HTTP " + it.statusCode + ", quotes=" + it.quotes.size + ", error=" + it.error) }
        assertTrue(snapshots.any { it.quotes.isNotEmpty() })
    }
}
