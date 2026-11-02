package com.example.tuan05project

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tuan05project.messaging.TodoMessagingService
import com.example.tuan05project.messaging.validTaskId
import com.example.tuan05project.navigation.AppNavigation
import com.example.tuan05project.navigation.NotificationOpen
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {
    private val pendingNotification = MutableStateFlow<NotificationOpen?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = bars, navigationBarStyle = bars)
        TodoMessagingService.createChannel(this)
        if (savedInstanceState == null) acceptIntent(intent)
        setContent { AppNavigation(this, pendingNotification) { pendingNotification.value = null } }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        acceptIntent(intent)
    }
    private fun acceptIntent(intent: Intent) {
        val uid = intent.getStringExtra("recipientUid")?.takeIf { it.length in 1..128 } ?: return
        pendingNotification.value = NotificationOpen(uid, validTaskId(intent.getStringExtra("taskId")))
        intent.removeExtra("recipientUid"); intent.removeExtra("taskId")
    }
}
