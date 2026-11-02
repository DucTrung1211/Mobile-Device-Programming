package com.example.tuan05project.features.todo.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuan05project.auth.AuthUser
import com.example.tuan05project.data.*
import com.example.tuan05project.features.todo.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TodoViewModel(
    private val repository: TaskRepository = FirebaseTaskRepository(),
    private val telemetry: Telemetry? = null,
    private val enableServices: Boolean = true
) : ViewModel(), TodoActions {
    private val mutable = MutableStateFlow(TodoUiState())
    val uiState = mutable.asStateFlow()
    private var sessionJob: Job? = null
    private var writeJob: Job? = null
    private var avatarJob: Job? = null
    private var configJob: Job? = null
    private var generation = 0
    fun bind(user: AuthUser?, integrated: Boolean) {
        if (mutable.value.user == user && mutable.value.integrated == integrated) return
        generation++
        sessionJob?.cancel(); writeJob?.cancel(); avatarJob?.cancel(); configJob?.cancel()
        mutable.value = TodoUiState(user = user, integrated = integrated, settings = mutable.value.settings)
        if (integrated && user != null) {
            retry()
            if (enableServices) { refreshConfig(); observeAvatar(user.uid) }
        }
    }
    fun retry() {
        val state = mutable.value
        val uid = state.user?.uid ?: return
        if (!state.integrated) return
        sessionJob?.cancel()
        mutable.update { it.copy(loading = true, error = null) }
        sessionJob = viewModelScope.launch {
            val timeout = launch {
                delay(15_000)
                mutable.update { if (it.loading) it.copy(loading = false, error = "Chưa nhận được dữ liệu máy chủ. Kiểm tra mạng, URL và rules rồi thử lại.") else it }
            }
            try {
                repository.observe(uid).collect { tasks ->
                    timeout.cancel()
                    mutable.update { it.copy(tasks = tasks.toList(), loading = false, error = null) }
                }
            } catch (error: CancellationException) { throw error
            } catch (error: Exception) {
                timeout.cancel()
                mutable.update { it.copy(tasks = emptyList(), loading = false, error = dataError(error)) }
            }
        }
    }
    fun openEditor(id: String? = null) {
        if (!mutable.value.integrated) { message("Tạo và đồng bộ công việc nằm ở Bài 02."); return }
        if (mutable.value.user == null) { message("Bạn cần đăng nhập Firebase để lưu công việc."); return }
        mutable.update { it.copy(editorOpen = true, editingId = id) }
    }
    fun closeEditor() { if (!mutable.value.busy) mutable.update { it.copy(editorOpen = false, editingId = null) } }
    fun saveTask(id: String?, draft: TaskDraft) {
        if (draft.title.isBlank() || draft.title.length > 200 || draft.note.length > 4000) {
            message("Nhập tên 1–200 ký tự và ghi chú tối đa 4000 ký tự."); return
        }
        write("Đã lưu công việc.") { uid ->
            repository.save(uid, id, draft)
            telemetry?.event(if (id == null) "task_create" else "task_edit", "category", draft.category.name.lowercase())
            mutable.update { it.copy(editorOpen = false, editingId = null) }
        }
    }
    override fun toggleTaskDone(id: String) {
        val task = mutable.value.findTask(id) ?: return
        write(if (task.isDone) "Đã bỏ đánh dấu." else "Đã hoàn thành công việc.") { uid ->
            repository.toggle(uid, task)
            if (!task.isDone) telemetry?.event("task_complete", "category", task.category.name.lowercase())
        }
    }
    fun deleteTask(id: String) = write("Đã xóa công việc.") { uid ->
        repository.delete(uid, id)
        mutable.update { it.copy(editorOpen = false, editingId = null) }
    }
    private fun write(success: String, action: suspend (String) -> Unit) {
        val state = mutable.value
        val uid = state.user?.uid ?: return
        if (!state.integrated || state.busy) return
        val epoch = generation
        mutable.update { it.copy(busy = true, message = null) }
        writeJob = viewModelScope.launch {
            try { action(uid); if (epoch == generation) message(success)
            } catch (_: TimeoutCancellationException) {
                if (epoch == generation) message("Chưa có xác nhận từ máy chủ. Kiểm tra dữ liệu sau khi kết nối lại trước khi thử lại.")
            } catch (error: CancellationException) { throw error
            } catch (error: Exception) { if (epoch == generation) message(dataError(error))
            } finally { if (epoch == generation) mutable.update { it.copy(busy = false) } }
        }
    }
    private fun observeAvatar(uid: String) {
        avatarJob = viewModelScope.launch {
            try {
                AvatarRepository().versions(uid).collectLatest { version ->
                    if (version != null) {
                        val bytes = AvatarRepository().read(uid)
                        mutable.update { it.copy(avatarBytes = bytes) }
                    }
                }
            } catch (error: CancellationException) { throw error
            } catch (_: Exception) { /* Account photo/initials remain available; task errors are shown separately. */ }
        }
    }
    fun uploadAvatar(context: Context, uri: Uri) {
        val uid = mutable.value.user?.uid ?: return
        if (!mutable.value.integrated) { message("Đổi ảnh với Storage nằm ở Bài 02."); return }
        if (mutable.value.busy || mutable.value.uploadProgress != null) return
        val epoch = generation
        mutable.update { it.copy(uploadProgress = 0f) }
        avatarJob?.cancel()
        avatarJob = viewModelScope.launch {
            try {
                val bytes = AvatarRepository().upload(context.applicationContext, uid, uri) { p ->
                    if (epoch == generation) mutable.update { it.copy(uploadProgress = p) }
                }
                if (epoch == generation) { mutable.update { it.copy(avatarBytes = bytes) }; message("Đã tải ảnh lên Storage.") }
            } catch (_: TimeoutCancellationException) { if (epoch == generation) message("Tải ảnh quá thời gian. Kiểm tra mạng và Storage.")
            } catch (error: CancellationException) { throw error
            } catch (error: Exception) { if (epoch == generation) message(dataError(error))
            } finally { if (epoch == generation) mutable.update { it.copy(uploadProgress = null) } }
        }
    }
    fun refreshConfig() {
        if (configJob?.isActive == true) return
        configJob = viewModelScope.launch {
            mutable.update { it.copy(configStatus = "Đang tải…") }
            try {
                val (limit, changed) = withTimeout(20_000) { RemoteConfigRepository().refresh() }
                mutable.update { it.copy(taskLimit = limit, configStatus = if (changed) "Đã kích hoạt giá trị mới" else "Fetch thành công; dùng giá trị hiện tại/mặc định") }
            } catch (_: TimeoutCancellationException) { mutable.update { it.copy(configStatus = "Quá thời gian; giữ mặc định 4") }
            } catch (error: CancellationException) { throw error
            } catch (_: Exception) { mutable.update { it.copy(configStatus = "Fetch lỗi; giữ giá trị hiện tại/mặc định") } }
        }
    }
    fun message(text: String) { mutable.update { it.copy(message = text) } }
    fun clearMessage() { mutable.update { it.copy(message = null) } }
    override fun selectTab(tab: TodoTab) { mutable.update { it.copy(selectedTab = tab) } }
    override fun showTasksOfCategory(category: TaskCategory?) { mutable.update { it.copy(categoryFilter = category, selectedTab = TodoTab.LIST) } }
    override fun updateCategoryFilter(category: TaskCategory?) { mutable.update { it.copy(categoryFilter = category) } }
    override fun updateSearchQuery(query: String) { mutable.update { it.copy(searchQuery = query) } }
    override fun updateThemeMode(mode: ThemeMode) { mutable.update { it.copy(settings = it.settings.copy(themeMode = mode)) } }
    override fun updateAccentColor(color: AccentColor) { mutable.update { it.copy(settings = it.settings.copy(accentColor = color)) } }
    override fun updateFontScale(scale: Float) { mutable.update { it.copy(settings = it.settings.copy(fontScale = scale.coerceIn(.85f, 1.15f))) } }
    override fun updateReminderEnabled(enabled: Boolean) { message("Nhắc việc tự động cần backend FCM; xem mục Dịch vụ Firebase.") }
    override fun updateSoundEnabled(enabled: Boolean) { mutable.update { it.copy(settings = it.settings.copy(isSoundOn = enabled)) } }
    override fun updateDailySummaryEnabled(enabled: Boolean) { message("Tổng kết lúc 21:00 cần bộ lập lịch trên backend; chưa được triển khai.") }
}
fun dataError(error: Exception): String = when {
    error.message?.contains("Permission denied", true) == true -> "Máy chủ từ chối quyền truy cập. Kiểm tra phiên đăng nhập và rules theo UID."
    error is IllegalArgumentException || error is IllegalStateException -> error.message ?: "Cấu hình hoặc dữ liệu không hợp lệ."
    else -> "Dịch vụ Firebase chưa sẵn sàng. Kiểm tra mạng, URL, rules và cấu hình Console."
}
