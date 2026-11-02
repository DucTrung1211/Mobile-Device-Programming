package com.example.tuan05project.features.todo.viewmodel

import com.example.tuan05project.auth.AuthUser
import com.example.tuan05project.features.todo.model.*
import java.time.LocalDate

data class TodoUiState(
    val user: AuthUser? = null,
    val integrated: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val settings: TodoSettings = TodoSettings(),
    val selectedTab: TodoTab = TodoTab.HOME,
    val searchQuery: String = "",
    val categoryFilter: TaskCategory? = null,
    val loading: Boolean = false,
    val busy: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val editorOpen: Boolean = false,
    val editingId: String? = null,
    val avatarBytes: ByteArray? = null,
    val uploadProgress: Float? = null,
    val taskLimit: Int = 4,
    val configStatus: String = "Chưa tải",
    val today: LocalDate = LocalDate.now()
) {
    val account get() = user?.let { UserAccount(it.displayName, it.email.orEmpty(), "google.com" in it.providers) }
    val avatarUri: Any? get() = avatarBytes ?: user?.photoUrl
    val todayTasks get() = tasks.filter { it.date == today }
    val todayDoneCount get() = todayTasks.count { it.isDone }
    val todayProgressPercent get() = percent(todayDoneCount, todayTasks.size)
    val pendingCount get() = tasks.count { !it.isDone }
    val filteredPendingTasks get() = filtered().filter { !it.isDone }
    val filteredDoneTasks get() = filtered().filter { it.isDone }
    val hasReminderAlert get() = false // Only actual FCM arrivals are shown in the notification view.
    fun remainingCount(category: TaskCategory) = tasks.count { !it.isDone && it.category == category }
    fun findTask(id: String) = tasks.find { it.id == id }
    private fun filtered() = tasks.filter { (categoryFilter == null || it.category == categoryFilter) &&
        (it.title.contains(searchQuery, true) || it.note.contains(searchQuery, true)) }
}
fun percent(done: Int, total: Int): Int = if (total <= 0) 0 else (done * 100 / total).coerceIn(0, 100)

interface TodoActions {
    fun selectTab(tab: TodoTab)
    fun showTasksOfCategory(category: TaskCategory?)
    fun updateCategoryFilter(category: TaskCategory?)
    fun updateSearchQuery(query: String)
    fun toggleTaskDone(id: String)
    fun updateThemeMode(mode: ThemeMode)
    fun updateAccentColor(color: AccentColor)
    fun updateFontScale(scale: Float)
    fun updateReminderEnabled(enabled: Boolean)
    fun updateSoundEnabled(enabled: Boolean)
    fun updateDailySummaryEnabled(enabled: Boolean)
}
