package com.example.tuan05project.preview

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuan05project.auth.AuthUser
import com.example.tuan05project.features.todo.model.*
import com.example.tuan05project.features.todo.ui.account.*
import com.example.tuan05project.features.todo.ui.main.*
import com.example.tuan05project.features.todo.ui.task.TaskFormScreen
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import com.example.tuan05project.features.todo.viewmodel.*
import java.time.LocalDate
import java.time.LocalTime

// Design fixtures only: debug previews are never used by app navigation or Firebase repositories.
private val today = LocalDate.of(2026, 11, 2)
private val previewState = TodoUiState(
    user = AuthUser("preview-only", "Tài khoản xem trước", "preview@example.test", null, listOf("google.com")),
    integrated = true, today = today,
    tasks = listOf(
        Task("preview-1", "Nộp bài tập Lập trình di động", "", TaskCategory.STUDY, today, LocalTime.of(9, 0), false, TaskPriority.HIGH, completedDate = today),
        Task("preview-2", "Ôn chương 3 – Cấu trúc dữ liệu", "", TaskCategory.STUDY, today, LocalTime.of(14, 0), false, TaskPriority.MEDIUM),
        Task("preview-3", "Đi chợ mua đồ cho tuần", "", TaskCategory.LIFE, today, LocalTime.of(17, 30), false, TaskPriority.LOW),
        Task("preview-4", "Tập thể dục 30 phút", "", TaskCategory.LIFE, today, LocalTime.of(6, 30), false, TaskPriority.LOW, completedDate = today)
    )
)
private object PreviewActions : TodoActions {
    override fun selectTab(tab: TodoTab) {}
    override fun showTasksOfCategory(category: TaskCategory?) {}
    override fun updateCategoryFilter(category: TaskCategory?) {}
    override fun updateSearchQuery(query: String) {}
    override fun toggleTaskDone(id: String) {}
    override fun updateThemeMode(mode: ThemeMode) {}
    override fun updateAccentColor(color: AccentColor) {}
    override fun updateFontScale(scale: Float) {}
    override fun updateReminderEnabled(enabled: Boolean) {}
    override fun updateSoundEnabled(enabled: Boolean) {}
    override fun updateDailySummaryEnabled(enabled: Boolean) {}
}
@Composable private fun MainFrame(tab: TodoTab, content: @Composable () -> Unit) {
    TodoTheme(previewState.settings) {
        Column(Modifier.fillMaxSize().statusBarsPadding()) {
            Box(Modifier.weight(1f)) { content() }
            TodoBottomBar(tab, {}, {})
        }
    }
}
@Preview(widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable fun LoginPreview() { TodoTheme(previewState.settings) { LoginScreen(false, {}, {}, {}) } }

@Preview(widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable fun HomePreview() { MainFrame(TodoTab.HOME) { TodoHomeScreen(previewState, PreviewActions, {}, {}) } }

@Preview(widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable fun ListPreview() { MainFrame(TodoTab.LIST) { TaskListScreen(previewState, PreviewActions, {}) } }

@Preview(widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable fun FormPreview() { TodoTheme(previewState.settings) { TaskFormScreen(previewState, null, {}, { _, _ -> }, {}) } }

@Preview(widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable fun ProfilePreview() { TodoTheme(previewState.settings) { ProfileScreen(previewState, {}, {}, {}, {}) } }

@Preview(widthDp = 390, heightDp = 844, showSystemUi = true)
@Composable fun SettingsPreview() { MainFrame(TodoTab.SETTINGS) { SettingsScreen(previewState, PreviewActions, {}) } }
