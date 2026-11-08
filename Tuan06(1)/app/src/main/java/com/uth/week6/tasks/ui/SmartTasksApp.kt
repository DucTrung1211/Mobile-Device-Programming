package com.uth.week6.tasks.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.uth.week6.tasks.features.todo.model.TodoTab
import com.uth.week6.tasks.features.todo.ui.common.TodoSnackbarHost
import com.uth.week6.tasks.features.todo.ui.main.*
import com.uth.week6.tasks.features.todo.ui.task.TaskFormScreen
import com.uth.week6.tasks.features.todo.ui.theme.TodoTheme
import com.uth.week6.tasks.features.todo.viewmodel.TodoViewModel

@Composable
fun SmartTasksApp(vm: TodoViewModel) {
    val state by vm.state.collectAsStateWithLifecycle()
    val owner = LocalLifecycleOwner.current
    LaunchedEffect(owner) {
        owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) { vm.refreshDate() }
    }
    TodoTheme(state.settings) {
        val back = {
            if (state.route == "form" && state.selectedId != null) vm.openTask(state.selectedId!!)
            else vm.back()
        }
        BackHandler(state.onboarded && (state.route != "main" || state.selectedTab != TodoTab.HOME)) {
            if (state.route != "main") back() else vm.selectTab(TodoTab.HOME)
        }
        if (!state.onboarded) {
            OnboardingScreen(state.onboardingPage, vm::onboarding, vm::finishOnboarding)
            return@TodoTheme
        }
        Box(Modifier.fillMaxSize().background(TodoTheme.colors.background).statusBarsPadding()) {
            Column(Modifier.fillMaxSize()) {
                if (state.loading || state.busy) LinearProgressIndicator(Modifier.fillMaxWidth())
                if (state.error != null) {
                    Surface(color = MaterialTheme.colorScheme.errorContainer) {
                        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(state.error!!, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                            TextButton(onClick = vm::refresh, enabled = !state.busy && !state.loading) { Text("Thử lại") }
                        }
                    }
                }
                when (state.route) {
                    "form" -> key(state.selectedId) {
                        TaskFormScreen(state, state.selectedId, back, vm::saveTask, vm::deleteTask)
                    }
                    "detail" -> TaskDetailScreen(state, vm, vm::back)
                    "profile" -> ProfileScreen(state, vm::back, vm::refresh)
                    else -> {
                        Box(Modifier.weight(1f).imePadding()) {
                            when (state.selectedTab) {
                                TodoTab.HOME -> if (state.loading && state.tasks.isEmpty()) StatusPanel("Đang tải công việc…")
                                    else TodoHomeScreen(state, vm, vm::openTask, vm::showProfile)
                                TodoTab.LIST -> if (state.loading && state.tasks.isEmpty()) StatusPanel("Đang tải công việc…")
                                    else TaskListScreen(state, vm, vm::openTask)
                                TodoTab.NOTIFICATIONS -> RemindersScreen(state, vm::openTask)
                                TodoTab.SETTINGS -> SettingsScreen(state, vm, vm::showProfile)
                            }
                        }
                        TodoBottomBar(state.selectedTab, vm::selectTab) { vm.edit(null) }
                    }
                }
            }
            TodoSnackbarHost(
                message = state.message,
                onMessageShown = vm::clearMessage,
                modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding().padding(bottom = 80.dp)
            )
        }
    }
}

@Composable
fun StatusPanel(message: String, retry: (() -> Unit)? = null) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(message)
        if (retry != null) TextButton(onClick = retry) { Text("Thử lại") }
    }
}
