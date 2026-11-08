package com.uth.week6.permissions.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.uth.week6.permissions.MainActivity
import com.uth.week6.permissions.model.PermissionStep

const val CHANNEL_ID = "permission_test"

class PermissionRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("permission_flow", Context.MODE_PRIVATE)

    fun createChannel() {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID, "Permission test", NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    fun granted(step: PermissionStep): Boolean = when (step) {
        PermissionStep.LOCATION -> has(Manifest.permission.ACCESS_COARSE_LOCATION) || has(Manifest.permission.ACCESS_FINE_LOCATION)
        PermissionStep.NOTIFICATION -> (Build.VERSION.SDK_INT < 33 || has(Manifest.permission.POST_NOTIFICATIONS)) &&
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        PermissionStep.CAMERA -> has(Manifest.permission.CAMERA)
    }

    fun label(step: PermissionStep): String = when {
        !granted(step) -> "Not allowed"
        step == PermissionStep.LOCATION -> if (has(Manifest.permission.ACCESS_FINE_LOCATION)) "Precise location" else "Approximate location"
        step == PermissionStep.NOTIFICATION && channelBlocked() -> "Allowed · test channel disabled"
        else -> "Allowed"
    }

    private fun has(permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    fun asked(step: PermissionStep) = prefs.getBoolean("asked_" + step.name, false)
    fun markAsked(step: PermissionStep) = prefs.edit().putBoolean("asked_" + step.name, true).apply()
    fun savedStep() = prefs.getInt("step", 0).coerceIn(0, 3)
    fun saveStep(step: Int) = prefs.edit().putInt("step", step).apply()

    fun channelBlocked(): Boolean =
        context.getSystemService(NotificationManager::class.java).getNotificationChannel(CHANNEL_ID)?.importance == NotificationManager.IMPORTANCE_NONE

    fun sendTest(): String {
        // Luôn kiểm tra lại quyền ở thời điểm thực hiện thao tác.
        if (!granted(PermissionStep.NOTIFICATION)) return "Notifications are not allowed. Enable them first."
        if (channelBlocked()) return "The test notification channel is disabled. Enable it in Settings."
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Notification permission works")
            .setContentText("This is a real notification from Android Permissions.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        return try {
            NotificationManagerCompat.from(context).notify(1001, notification)
            "Test notification sent. Check the notification shade."
        } catch (error: SecurityException) {
            "Permission changed. Please check Settings."
        }
    }
}
