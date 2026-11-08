package com.uth.week6.tasks.features.todo.viewmodel

import com.uth.week6.tasks.features.todo.model.*
import java.time.LocalDate

data class TodoUiState(
    val tasks: List<Task> = emptyList(),
    val settings: TodoSettings = TodoSettings(),
    val selectedTab: TodoTab = TodoTab.HOME,
    val searchQuery: String = "",
    val categoryFilter: TaskCategory? = null,
    val loading: Boolean = false,
    val busy: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val today: LocalDate = LocalDate.now(),
    val taskLimit: Int = Int.MAX_VALUE,
    val route: String = "main",
    val selectedId: String? = null,
    val detailLoading: Boolean = false,
    val detailError: String? = null,
    val detail: Task? = null,
    val onboarded: Boolean = false,
    val onboardingPage: Int = 0
) {
    val account = UserAccount("bạn", "", false)
    val avatarUri: Any? = null
    val todayTasks get() = tasks.filter { it.date == today }.sortedBy { it.time }
    val todayDoneCount get() = todayTasks.count { it.isDone }
    val todayProgressPercent get() = percent(todayDoneCount, todayTasks.size)
    val pendingCount get() = tasks.count { !it.isDone }
    val filteredPendingTasks get() = filtered().filter { !it.isDone }
    val filteredDoneTasks get() = filtered().filter { it.isDone }
    val hasReminderAlert get() = false
    fun remainingCount(category: TaskCategory) = tasks.count { !it.isDone && it.category == category }
    fun findTask(id: String) = detail?.takeIf { it.id == id } ?: tasks.find { it.id == id }
    private fun filtered() = tasks.filter {
        (categoryFilter == null || it.category == categoryFilter) &&
            (it.title.contains(searchQuery, true) || it.note.contains(searchQuery, true))
    }.sortedWith(compareBy({ it.date }, { it.time }))
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
