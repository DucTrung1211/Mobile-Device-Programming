package com.example.tuan04project.features.dataflow.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

const val OTP_LENGTH = 5

data class PasswordResetResult(
    val email: String,
    val otp: String,
    val password: String
)

class DataFlowViewModel : ViewModel() {
    private val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    var email by mutableStateOf("")
        private set
    var otp by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    var emailError by mutableStateOf<String?>(null)
        private set
    var otpError by mutableStateOf<String?>(null)
        private set
    var passwordError by mutableStateOf<String?>(null)
        private set
    var confirmError by mutableStateOf<String?>(null)
        private set

    var submittedResult by mutableStateOf<PasswordResetResult?>(null)
        private set

    fun updateEmail(value: String) {
        email = value
        emailError = null
    }

    fun updateOtp(value: String) {
        val isOnlyDigits = value.all { character -> character.isDigit() }
        if (value.length <= OTP_LENGTH && isOnlyDigits) {
            otp = value
            otpError = null
        }
    }

    fun updateNewPassword(value: String) {
        newPassword = value
        passwordError = null
    }

    fun updateConfirmPassword(value: String) {
        confirmPassword = value
        passwordError = null
    }

    fun submitEmail(): Boolean {
        val cleanEmail = email.trim()
        emailError = when {
            cleanEmail.isEmpty() -> "Please enter your email"
            !emailPattern.matches(cleanEmail) -> "Email format is not valid"
            else -> null
        }
        email = cleanEmail
        return emailError == null
    }

    fun submitOtp(): Boolean {
        otpError = if (otp.length == OTP_LENGTH) null else "Please enter all $OTP_LENGTH digits"
        return otpError == null
    }

    fun submitNewPassword(): Boolean {
        passwordError = when {
            newPassword.isEmpty() || confirmPassword.isEmpty() -> "Please fill in both password fields"
            newPassword != confirmPassword -> "Passwords do not match"
            else -> null
        }
        return passwordError == null
    }

    fun submit(): Boolean {
        val isDataValid = emailPattern.matches(email) &&
            otp.length == OTP_LENGTH &&
            newPassword.isNotEmpty() &&
            newPassword == confirmPassword
        if (!isDataValid) {
            confirmError = "Information is incomplete, please go back and check"
            return false
        }
        confirmError = null
        submittedResult = PasswordResetResult(email = email, otp = otp, password = newPassword)
        return true
    }
}
