package com.uth.week6.crypto.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.uth.week6.crypto.data.CryptoRepository
import com.uth.week6.crypto.model.*
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CryptoViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences("favorites", 0)
    private val repository = CryptoRepository()
    private val loopMutex = Mutex()
    private val mutableState = MutableStateFlow(MarketState(
        favorites = preferences.getStringSet("coins", setOf("ETH", "BTC"))!!.toSet()
    ))
    val state = mutableState.asStateFlow()

    // repeatOnLifecycle hủy vòng lặp và HTTP khi app xuống nền.
    suspend fun pollWhileStarted() = loopMutex.withLock {
        try {
            while (currentCoroutineContext().isActive) {
                mutableState.update { it.copy(polling = true, now = System.currentTimeMillis()) }
                val snapshots = repository.poll()
                mutableState.update { old ->
                    val histories = old.histories.toMutableMap()
                    snapshots.filter { it.error == null }.flatMap { it.quotes }.forEach { quote ->
                        val key = quote.source + ":" + quote.coin.name
                        val samples = histories[key].orEmpty()
                        if (samples.lastOrNull()?.receivedAt != quote.receivedAt) {
                            histories[key] = (samples + Sample(quote.receivedAt, quote.price)).takeLast(60)
                        }
                    }
                    old.copy(sources = snapshots, histories = histories, loading = false, polling = false, now = System.currentTimeMillis())
                }
                // Nghỉ sau khi cả lượt hoàn tất; mạng chậm không làm chồng request.
                delay(3000)
            }
        } finally {
            mutableState.update { it.copy(polling = false) }
        }
    }

    fun favorite(coin: Coin) {
        mutableState.update {
            val favorites = it.favorites.toMutableSet()
            if (!favorites.add(coin.name)) favorites.remove(coin.name)
            it.copy(favorites = favorites)
        }
        preferences.edit().putStringSet("coins", state.value.favorites).apply()
    }
    fun open(coin: Coin) = mutableState.update { it.copy(selected = coin) }
    fun close() = mutableState.update { it.copy(selected = null) }
}
