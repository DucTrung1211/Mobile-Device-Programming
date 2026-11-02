package com.example.tuan05project.auth

import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException

fun authErrorMessage(error: Exception): String = when (error) {
    is GetCredentialCancellationException -> "Bạn đã hủy đăng nhập Google."
    is NoCredentialException -> "Chưa có tài khoản Google khả dụng. Hãy thêm tài khoản trên thiết bị."
    is GetCredentialProviderConfigurationException -> "Thiếu dịch vụ Credential Manager hoặc Google Play Services."
    is FirebaseNetworkException -> "Không có kết nối mạng. Vui lòng thử lại."
    is FirebaseAuthException -> when (error.errorCode) {
        "ERROR_OPERATION_NOT_ALLOWED", "ERROR_INVALID_CREDENTIAL", "ERROR_INVALID_CERT_HASH" ->
            "Cấu hình Google/Firebase chưa hợp lệ. Kiểm tra provider, SHA-1/SHA-256 và Web client ID."
        else -> "Firebase từ chối xác thực (${error.errorCode}). Vui lòng thử lại."
    }
    is GetCredentialException -> "Không lấy được thông tin Google. Kiểm tra mạng, SHA và Web client ID."
    is IllegalStateException -> error.message ?: "Thiếu cấu hình Firebase."
    else -> "Không thể đăng nhập. Vui lòng thử lại."
}
