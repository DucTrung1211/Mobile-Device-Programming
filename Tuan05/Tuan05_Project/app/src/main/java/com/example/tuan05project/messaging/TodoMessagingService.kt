package com.example.tuan05project.messaging

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import com.example.tuan05project.MainActivity
import com.example.tuan05project.R
import com.example.tuan05project.data.TokenRegistry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

data class ReceivedNotice(val uid: String, val taskId: String?, val time: Long)
object FcmInbox {
    private val mutable = MutableStateFlow<List<ReceivedNotice>>(emptyList())
    val notices = mutable.asStateFlow()
    fun load(context: Context) {
        val raw = context.getSharedPreferences("fcm_inbox", Context.MODE_PRIVATE).getString("notices", "[]")
        mutable.value = runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).map { index ->
                val item = array.getJSONObject(index)
                ReceivedNotice(item.getString("uid"), item.optString("taskId").takeIf { it.isNotEmpty() }, item.getLong("time"))
            }
        }.getOrDefault(emptyList())
    }
    fun add(context: Context, notice: ReceivedNotice) {
        load(context)
        mutable.value = (listOf(notice) + mutable.value).take(30)
        val array = JSONArray()
        mutable.value.forEach { array.put(JSONObject().put("uid", it.uid).put("taskId", it.taskId ?: "").put("time", it.time)) }
        context.getSharedPreferences("fcm_inbox", Context.MODE_PRIVATE).edit().putString("notices", array.toString()).apply()
    }
}
class TodoMessagingService : FirebaseMessagingService() {
    override fun onRegistered(installationId: String) { TokenRegistry(this).refreshed(installationId, "fid") }
    @Suppress("OVERRIDE_DEPRECATION")
    override fun onNewToken(token: String) { TokenRegistry(this).refreshed(token, "token") }
    override fun onMessageReceived(message: RemoteMessage) {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        // Require recipientUid for private task notifications. No arbitrary URLs or destinations.
        if (message.data["recipientUid"] != user.uid) return
        val task = validTaskId(message.data["taskId"])
        FcmInbox.add(this, ReceivedNotice(user.uid, task, System.currentTimeMillis()))
        createChannel(this)
        if (android.os.Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("recipientUid", user.uid); putExtra("taskId", task); putExtra("openFirebase", true)
        }
        val id = message.messageId?.hashCode() ?: System.currentTimeMillis().toInt()
        val pending = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL)
            .setSmallIcon(R.drawable.ic_check).setContentTitle("UTH Todo")
            .setContentText("Bạn có cập nhật công việc. Nhấn để xem.")
            .setContentIntent(pending).setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE).build()
        getSystemService(NotificationManager::class.java).notify(id, notification)
    }
    companion object {
        const val CHANNEL = "todo_updates"
        fun createChannel(context: Context) {
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CHANNEL, "Cập nhật công việc", NotificationManager.IMPORTANCE_DEFAULT))
        }
    }
}
fun validTaskId(value: String?): String? = value?.takeIf { it.matches(Regex("[A-Za-z0-9_-]{1,128}")) }
