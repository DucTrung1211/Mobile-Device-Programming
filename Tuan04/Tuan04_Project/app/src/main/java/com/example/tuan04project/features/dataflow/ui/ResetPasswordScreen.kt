package com.example.tuan04project.features.dataflow.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tuan04project.R

@Composable
fun ResetPasswordScreen(
    newPassword: String,
    confirmPassword: String,
    passwordError: String?,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onBack: () -> Unit,
    onNextClick: () -> Unit
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    DataFlowScreenLayout(
        title = "Create new password",
        description = "Your new password must be different from previously used password",
        onBack = onBack
    ) {
        DataFlowTextField(
            value = newPassword,
            onValueChange = onNewPasswordChange,
            placeholder = "Password",
            iconRes = R.drawable.ic_lock,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible }
        )
        Spacer(Modifier.height(14.dp))
        DataFlowTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = "Confirm Password",
            iconRes = R.drawable.ic_lock,
            keyboardType = KeyboardType.Password,
            isPassword = true,
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible }
        )
        ErrorText(message = passwordError)
        Spacer(Modifier.height(24.dp))
        DataFlowButton(text = "Next", onClick = onNextClick)
    }
}
