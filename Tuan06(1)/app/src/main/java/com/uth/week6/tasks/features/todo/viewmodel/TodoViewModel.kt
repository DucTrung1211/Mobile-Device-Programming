package com.uth.week6.tasks.features.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.uth.week6.tasks.BuildConfig
import com.uth.week6.tasks.data.*
import com.uth.week6.tasks.features.todo.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class TodoViewModel(application: Application) : AndroidViewModel(application), TodoActions {
    private val prefs = application.getSharedPreferences("tasks_preferences", 0)
    private val repository = TaskRepository(taskApi(BuildConfig.TASK_TOKEN))
    private val mutableState = MutableStateFlow(TodoUiState(
        onboarded = prefs.getBoolean("onboarded", false),
        settings = TodoSettings(
            themeMode = ThemeMode.valueOf(prefs.getString("theme", "LIGHT")!!),
            accentColor = AccentColor.valueOf(prefs.getString("accent", "BLUE")!!),
            fontScale = prefs.getFloat("font", 1f),
            isSoundOn = prefs.getBoolean("sound", true)
        )
    ))
    val state = mutableState.asStateFlow()
    private var detailJob: Job? = null
    private var refreshJob: Job? = null

    init { refresh() }

    fun refresh() {
        if (refreshJob?.isActive == true || state.value.busy) return
        refreshJob = viewModelScope.launch {
            mutableState.update { it.copy(loading = true, error = null, today = LocalDate.now()) }
            try {
                if (BuildConfig.TASK_TOKEN.isBlank()) throw ApiFailure("Chưa cấu hình TASK_TOKEN trong secrets.properties. Xem README.")
                val tasks = repository.list()
                mutableState.update { it.copy(tasks = tasks) }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                mutableState.update { it.copy(error = readable(error)) }
            } finally {
                mutableState.update { it.copy(loading = false) }
            }
        }
    }

    fun refreshDate() = mutableState.update { it.copy(today = LocalDate.now()) }

    fun openTask(id: String) {
        detailJob?.cancel()
        mutableState.update { it.copy(route = "detail", selectedId = id, detail = null, detailLoading = true, detailError = null) }
        detailJob = viewModelScope.launch {
            try {
                val task = repository.get(id)
                mutableState.update { current ->
                    current.copy(detail = task, tasks = current.tasks.map { if (it.id == id) task else it })
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                mutableState.update { it.copy(detailError = readable(error)) }
            } finally {
                mutableState.update { it.copy(detailLoading = false) }
            }
        }
    }

    fun edit(id: String?) {
        if (!state.value.busy) mutableState.update { it.copy(route = "form", selectedId = id) }
    }
    fun showProfile() = mutableState.update { it.copy(route = "profile") }
    fun back() {
        if (state.value.busy) return
        detailJob?.cancel()
        mutableState.update { it.copy(route = "main", selectedId = null, detailLoading = false, detailError = null) }
    }
    fun clearMessage() = mutableState.update { it.copy(message = null) }

    private fun mutate(action: suspend () -> List<Task>, close: Boolean = false) {
        if (state.value.busy || state.value.loading || state.value.detailLoading) return
        mutableState.update { it.copy(busy = true, error = null, message = null) }
        viewModelScope.launch {
            try {
                val tasks = action()
                mutableState.update {
                    it.copy(tasks = tasks, detail = tasks.find { task -> task.id == it.selectedId },
                        route = if (close) "main" else it.route, message = "Đã đọc lại và xác nhận dữ liệu từ API.")
                }
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                mutableState.update { it.copy(error = readable(error), message = readable(error)) }
                // Nếu mock không lưu, khôi phục danh sách thật; không giữ thay đổi giả.
                try {
                    val tasks = repository.list()
                    mutableState.update { it.copy(tasks = tasks) }
                } catch (retryError: Exception) {
                    if (retryError is CancellationException) throw retryError
                }
            } finally {
                mutableState.update { it.copy(busy = false) }
            }
        }
    }

    fun saveTask(id: String?, draft: TaskDraft) {
        if (draft.title.isBlank()) return
        val old = id?.let { state.value.findTask(it) }
        val task = Task(id ?: "", draft.title.trim(), draft.note, draft.category, draft.date,
            draft.time, draft.isReminderOn, draft.priority, subtasks = old?.subtasks.orEmpty(),
            isDone = old?.isDone ?: false)
        mutate({ repository.save(task, id == null) }, close = true)
    }

    override fun toggleTaskDone(id: String) {
        val task = state.value.findTask(id) ?: return
        mutate({ repository.save(task.copy(isDone = !task.isDone), false) })
    }
    fun deleteTask(id: String) = mutate({ repository.delete(id) }, close = true)

    fun changeSubtask(id: String, index: Int?, title: String? = null, delete: Boolean = false) {
        val task = state.value.findTask(id) ?: return
        val subtasks = task.subtasks.toMutableList()
        when {
            index == null && !title.isNullOrBlank() -> subtasks.add(Subtask(subtasks.size, title.trim(), false))
            index != null && index in subtasks.indices -> {
                val old = subtasks[index]
                if (delete) subtasks.removeAt(index)
                else subtasks[index] = if (title == null) old.copy(isDone = !old.isDone) else old.copy(title = title.trim())
            }
            else -> return
        }
        mutate({ repository.save(task.copy(subtasks = subtasks.mapIndexed { i, item -> item.copy(id = i) }), false) })
    }

    override fun selectTab(tab: TodoTab) = mutableState.update { it.copy(selectedTab = tab, route = "main") }
    override fun showTasksOfCategory(category: TaskCategory?) = mutableState.update {
        it.copy(selectedTab = TodoTab.LIST, categoryFilter = category, searchQuery = "", route = "main")
    }
    override fun updateCategoryFilter(category: TaskCategory?) = mutableState.update { it.copy(categoryFilter = category) }
    override fun updateSearchQuery(query: String) = mutableState.update { it.copy(searchQuery = query) }

    private fun settings(change: (TodoSettings) -> TodoSettings) {
        mutableState.update { it.copy(settings = change(it.settings)) }
        val value = state.value.settings
        prefs.edit().putString("theme", value.themeMode.name).putString("accent", value.accentColor.name)
            .putFloat("font", value.fontScale).putBoolean("sound", value.isSoundOn).apply()
    }
    override fun updateThemeMode(mode: ThemeMode) = settings { it.copy(themeMode = mode) }
    override fun updateAccentColor(color: AccentColor) = settings { it.copy(accentColor = color) }
    override fun updateFontScale(scale: Float) = settings { it.copy(fontScale = scale.coerceIn(0.85f, 1.2f)) }
    override fun updateSoundEnabled(enabled: Boolean) = settings { it.copy(isSoundOn = enabled) }
    override fun updateReminderEnabled(enabled: Boolean) {
        mutableState.update { it.copy(message = "remind chỉ được lưu trên công việc. Chưa có bộ lập lịch thông báo nền.") }
    }
    override fun updateDailySummaryEnabled(enabled: Boolean) {
        mutableState.update { it.copy(message = "Chưa có bộ lập lịch tổng kết cuối ngày.") }
    }
    fun onboarding(page: Int) = mutableState.update { it.copy(onboardingPage = page.coerceIn(0, 2)) }
    fun finishOnboarding() {
        prefs.edit().putBoolean("onboarded", true).apply()
        mutableState.update { it.copy(onboarded = true) }
    }
    private fun readable(error: Exception): String = when (error) {
        is java.net.SocketTimeoutException -> "Kết nối quá thời gian. Hãy thử lại."
        is java.net.UnknownHostException -> "Không kết nối được máy chủ. Kiểm tra mạng rồi thử lại."
        is ApiFailure, is UnverifiedWrite -> error.message.orEmpty()
        is java.io.IOException -> "Lỗi mạng. Hãy kiểm tra kết nối và thử lại."
        else -> "Dữ liệu API không hợp lệ: " + (error.message ?: error.javaClass.simpleName)
    }
}
