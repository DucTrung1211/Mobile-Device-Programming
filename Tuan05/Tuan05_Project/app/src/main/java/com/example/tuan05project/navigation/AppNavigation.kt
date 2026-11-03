package com.example.tuan05project.navigation

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuan05project.auth.*
import com.example.tuan05project.features.legacy.LegacyAuthScreen
import com.example.tuan05project.features.lessons.LessonPickerScreen
import com.example.tuan05project.features.services.*
import com.example.tuan05project.features.todo.model.TodoTab
import com.example.tuan05project.features.todo.ui.account.ProfileScreen
import com.example.tuan05project.features.todo.ui.common.TodoSnackbarHost
import com.example.tuan05project.features.todo.ui.main.*
import com.example.tuan05project.features.todo.ui.task.TaskFormScreen
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import com.example.tuan05project.features.todo.viewmodel.TodoViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withTimeout

data class NotificationOpen(val uid: String, val taskId: String?)

@Composable
fun AppNavigation(activity: Activity, notification: StateFlow<NotificationOpen?>, consume: () -> Unit) {
    val auth: AuthViewModel = viewModel()
    val services: ServicesViewModel = viewModel()
    val todo: TodoViewModel = viewModel(factory = remember {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = TodoViewModel(telemetry = services.telemetry) as T
        }
    })
    val authState by auth.uiState.collectAsStateWithLifecycle()
    val state by todo.uiState.collectAsStateWithLifecycle()
    val serviceState by services.state.collectAsStateWithLifecycle()
    val notices by services.notices.collectAsStateWithLifecycle()
    val pending by notification.collectAsStateWithLifecycle()
    var lesson by rememberSaveable { mutableStateOf(Lesson.PICKER) }
    var guest by rememberSaveable(lesson, authState.user?.uid) { mutableStateOf(false) }
    var route by rememberSaveable(lesson, authState.user?.uid, guest) { mutableStateOf("home") }
    var pendingTask by rememberSaveable { mutableStateOf<String?>(null) }
    var requestedUid by rememberSaveable { mutableStateOf<String?>(null) }
    val integrated = lesson == Lesson.FIREBASE && authState.user != null
    val destination = destinationFor(lesson, authState.restoring, authState.user != null, guest, route)
    val google = remember { GoogleSignInManager() }
    val logout = {
        guest = false
        auth.signOut(
            clearCredential = { withTimeout(8_000) { google.clear(activity.applicationContext) } },
            beforeSignOut = {
                activity.getSystemService(NotificationManager::class.java).cancelAll()
                services.revoke()
            })
    }

    LaunchedEffect(lesson, authState.user) { todo.bind(authState.user, integrated) }
    LaunchedEffect(pending) {
        pending?.let {
            requestedUid = it.uid; pendingTask = it.taskId; lesson = Lesson.FIREBASE
            consume()
        }
    }
    LaunchedEffect(authState.user?.uid, requestedUid, state.tasks, state.loading) {
        val uid = authState.user?.uid
        if (requestedUid != null && uid != null && !state.loading && state.user?.uid == uid) {
            if (uid == requestedUid) {
                if (pendingTask != null && state.findTask(pendingTask!!) != null) todo.openEditor(pendingTask)
                else { todo.selectTab(TodoTab.LIST); todo.message("Đã mở danh sách. Công việc trong thông báo có thể đã bị xóa hoặc chưa tải được.") }
            } else todo.message("Thông báo thuộc tài khoản khác; không mở dữ liệu.")
            pendingTask = null; requestedUid = null
        }
    }
    LaunchedEffect(lesson, authState.user?.uid) {
        if (integrated) services.registerToken()
    }
    LaunchedEffect(destination, state.selectedTab, state.editorOpen) {
        services.screen(if (state.editorOpen) "task_form" else if (destination == Destination.HOME) state.selectedTab.name.lowercase() else destination.name.lowercase())
    }
    BackHandler(enabled = lesson != Lesson.PICKER) {
        if (!authState.loading && !state.busy) when {
            state.editorOpen -> todo.closeEditor()
            route != "home" -> route = "home"
            destination == Destination.HOME && state.selectedTab != TodoTab.HOME -> todo.selectTab(TodoTab.HOME)
            else -> lesson = Lesson.PICKER
        }
    }

    TodoTheme(state.settings) {
        Box(Modifier.fillMaxSize().background(TodoTheme.colors.background)) {
            when (destination) {
                Destination.LESSONS -> LessonPickerScreen(authState.user?.displayName) { lesson = it }
                Destination.RESTORING -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                Destination.LOGIN -> AuthScreen(authState, onLoginClick = {
                    auth.signIn({ google.requestGoogleIdToken(activity) }) { services.telemetry.event("login", "method", "google") }
                }, onBack = { if (!authState.loading) lesson = Lesson.PICKER }, onGuest = { guest = true })
                Destination.LEGACY -> LegacyAuthScreen(authState) {
                    auth.signIn({ google.requestGoogleIdToken(activity) }) { services.telemetry.event("login", "method", "google") }
                }
                else -> {
                    // Do not render a previous user's data while the binding effect is pending.
                    if (state.user?.uid != authState.user?.uid || state.integrated != integrated) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    } else if (state.editorOpen) {
                        TaskFormScreen(state, state.editingId, todo::closeEditor, todo::saveTask, todo::deleteTask)
                    } else when (destination) {
                        Destination.PROFILE -> ProfileScreen(state, { route = "home" }, logout,
                            { todo.uploadAvatar(activity.applicationContext, it) }, auth::rename)
                        Destination.SERVICES -> ServicesScreen(state, serviceState, notices, { route = "home" },
                            services::registerToken, todo::refreshConfig, todo::openEditor)
                        else -> Column(Modifier.fillMaxSize().statusBarsPadding()) {
                            LessonLabel(if (guest) "${lesson.code} · Phiên khách — không đồng bộ Firebase" else "${lesson.code} — ${lesson.title}") {
                                if (!state.busy) lesson = Lesson.PICKER
                            }
                            if (state.error != null) Row(Modifier.padding(horizontal = 20.dp)) {
                                Text(state.error!!, Modifier.weight(1f), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                                TextButton(onClick = todo::retry) { Text("Thử lại") }
                            }
                            if (state.loading) LinearProgressIndicator(Modifier.fillMaxWidth())
                            Box(Modifier.weight(1f)) {
                                when (state.selectedTab) {
                                    TodoTab.HOME -> TodoHomeScreen(state, todo, todo::openEditor, { route = "profile" })
                                    TodoTab.LIST -> TaskListScreen(state, todo, todo::openEditor)
                                    TodoTab.SETTINGS -> Column {
                                        Box(Modifier.weight(1f)) { SettingsScreen(state, todo) { route = "profile" } }
                                        TextButton(onClick = { if (integrated) route = "services" else todo.message("Dịch vụ Firebase nằm ở Bài 02 và cần tài khoản Firebase.") },
                                            modifier = Modifier.fillMaxWidth()) { Text("Dịch vụ Firebase · FCM, Remote Config, AdMob") }
                                    }
                                    TodoTab.NOTIFICATIONS -> if (state.integrated) ServicesScreen(state, serviceState, notices, { todo.selectTab(TodoTab.HOME) },
                                        services::registerToken, todo::refreshConfig, todo::openEditor)
                                    else Text("Nhận thông báo FCM nằm ở Bài 02. Phiên khách không nhận thông báo tài khoản.",
                                        Modifier.safeDrawingPadding().padding(20.dp))
                                }
                            }
                            TodoBottomBar(state.selectedTab, todo::selectTab, { todo.openEditor() })
                        }
                    }
                }
            }
            TodoSnackbarHost(
                message = authState.message ?: state.message,
                onMessageShown = { if (authState.message != null) auth.clearMessage() else todo.clearMessage() },
                modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 90.dp))
            if (state.busy) LinearProgressIndicator(Modifier.align(Alignment.TopCenter).statusBarsPadding().fillMaxWidth())
        }
    }
}
/** Small header that names the open exercise and leads back to the lesson picker. */
@Composable
private fun LessonLabel(text: String, onBack: () -> Unit) {
    TextButton(onClick = onBack, modifier = Modifier.padding(horizontal = 8.dp).heightIn(min = 32.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)) {
        Text("‹ $text", style = MaterialTheme.typography.labelSmall, color = TodoTheme.colors.textSecondary)
    }
}
