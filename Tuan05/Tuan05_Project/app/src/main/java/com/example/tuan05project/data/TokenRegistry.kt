package com.example.tuan05project.data

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ServerValue
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.util.UUID

/** Handles current FID registrations and legacy token refresh callbacks without logging identifiers. */
class TokenRegistry(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("fcm_registration", Context.MODE_PRIVATE)
    private val deviceId: String get() = prefs.getString("device", null) ?: UUID.randomUUID().toString().also {
        prefs.edit().putString("device", it).apply()
    }
    private fun record(identifier: String, kind: String) = mapOf(
        "identifier" to identifier, "kind" to kind, "updatedAt" to ServerValue.TIMESTAMP)
    private fun reference(uid: String) = FirebaseTaskRepository().database().getReference("users/$uid/deviceRegistrations/$deviceId")
    fun refreshed(identifier: String, kind: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        runCatching { reference(uid).setValue(record(identifier, kind)) }
        // register() also uploads and awaits the acknowledgement; it is retried on lesson entry.
    }
    // FirebaseMessaging register/unregister block internally and refuse the main thread.
    suspend fun register(): String = withContext(Dispatchers.IO) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@withContext "Cần đăng nhập để đăng ký thiết bị theo UID."
        val messaging = FirebaseMessaging.getInstance()
        messaging.isAutoInitEnabled = true
        withTimeout(20_000) { messaging.register().await() }
        val fid = withTimeout(20_000) { FirebaseInstallations.getInstance().id.await() }
        withTimeout(20_000) { reference(uid).setValue(record(fid, "fid")).await() }
        "Đã đăng ký FCM bằng FID và lưu theo UID. Chưa xác nhận nhận tin."
    }
    suspend fun revoke(): Unit = withContext(Dispatchers.IO) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        val removed = uid == null || withTimeoutOrNull(8_000) { reference(uid).removeValue().await(); true } == true
        val unregistered = withTimeoutOrNull(8_000) { FirebaseMessaging.getInstance().unregister().await(); true } == true
        check(removed && unregistered) { "Chưa xác nhận gỡ đăng ký FCM." }
    }
}
