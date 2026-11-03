package com.example.tuan05project.features.services

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuan05project.data.FirebaseTelemetry
import com.example.tuan05project.data.TokenRegistry
import com.example.tuan05project.messaging.FcmInbox
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ServicesState(val tokenStatus: String = "Chưa lấy token.", val working: Boolean = false)
class ServicesViewModel(application: Application) : AndroidViewModel(application) {
    private val mutable = MutableStateFlow(ServicesState())
    val state = mutable.asStateFlow()
    val notices = FcmInbox.notices
    val telemetry = FirebaseTelemetry(application)
    init { FcmInbox.load(application) }
    fun registerToken() {
        if (mutable.value.working) return
        mutable.value = ServicesState("Đang đăng ký…", true)
        viewModelScope.launch {
            try { mutable.value = ServicesState(TokenRegistry(getApplication()).register())
            } catch (error: CancellationException) { mutable.value = ServicesState("Đăng ký bị gián đoạn."); throw error
            } catch (error: Exception) {
                if (com.example.tuan05project.BuildConfig.DEBUG) android.util.Log.w("TodoFcm", "FCM registration failed: ${error.javaClass.name}: ${error.message}")
                mutable.value = ServicesState("Chưa đăng ký được token. Kiểm tra mạng, Realtime Database URL và rules.")
            }
        }
    }
    suspend fun revoke() = TokenRegistry(getApplication()).revoke()
    fun screen(name: String) { telemetry.screen(name) }
}
