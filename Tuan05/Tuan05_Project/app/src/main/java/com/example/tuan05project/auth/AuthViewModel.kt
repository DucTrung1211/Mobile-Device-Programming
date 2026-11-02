package com.example.tuan05project.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {
    private val mutable = MutableStateFlow(AuthUiState())
    val uiState = mutable.asStateFlow()
    init {
        viewModelScope.launch {
            repository.users.catch { mutable.update { it.copy(message = "Không khởi tạo được Firebase. Kiểm tra google-services.json.") } }
                .collect { user -> mutable.update { it.copy(user = user) } }
        }
    }
    fun signIn(token: suspend () -> String, onSuccess: () -> Unit = {}) {
        if (mutable.value.loading || mutable.value.user != null) return
        mutable.update { it.copy(loading = true, message = null) }
        viewModelScope.launch {
            try {
                val user = repository.signInWithGoogle(token())
                mutable.update { it.copy(user = user, loading = false, message = "Đăng nhập thành công.") }
                onSuccess()
            } catch (error: CancellationException) {
                mutable.update { it.copy(loading = false) }
                throw error
            } catch (error: Exception) {
                mutable.update { it.copy(loading = false, message = authErrorMessage(error)) }
            }
        }
    }
    fun signOut(clearCredential: suspend () -> Unit, beforeSignOut: suspend () -> Unit = {}) {
        if (mutable.value.loading) return
        mutable.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                var cleanupFailed = false
                try { beforeSignOut() } catch (error: CancellationException) { throw error } catch (_: Exception) { cleanupFailed = true }
                repository.signOut()
                mutable.value = AuthUiState(loading = true)
                try { clearCredential() } catch (error: CancellationException) { throw error } catch (_: Exception) { cleanupFailed = true }
                mutable.value = AuthUiState(message = if (cleanupFailed)
                    "Đã đăng xuất. Chưa xác nhận dọn token thông báo hoặc trạng thái chọn tài khoản; cần kiểm tra mạng/cấu hình."
                    else "Đã đăng xuất.")
            } catch (error: CancellationException) { throw error
            } catch (_: Exception) {
                repository.signOut()
                mutable.value = AuthUiState(message = "Đã đăng xuất Firebase. Không xóa được trạng thái chọn tài khoản Google; hãy chọn lại tài khoản khi đăng nhập.")
            }
        }
    }
    fun rename(name: String) {
        viewModelScope.launch {
            try {
                repository.rename(name)
                mutable.update { it.copy(user = it.user?.copy(name = name), message = "Đã cập nhật tên hiển thị.") }
            } catch (error: CancellationException) { throw error
            } catch (_: Exception) { mutable.update { it.copy(message = "Không cập nhật được hồ sơ. Hãy kiểm tra mạng và thử lại.") } }
        }
    }
    fun clearMessage() { mutable.update { it.copy(message = null) } }
}
