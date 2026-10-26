package com.example.tuan04project.features.dataflow.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tuan04project.R

@Composable
fun ConfirmScreen(
    email: String,
    otp: String,
    password: String,
    confirmError: String?,
    onBack: () -> Unit,
    onSubmitClick: () -> Unit
) {
    DataFlowScreenLayout(
        title = "Confirm",
        description = "We are here to help you!",
        onBack = onBack
    ) {
        DataFlowTextField(
            value = email,
            onValueChange = {},
            placeholder = "Email",
            iconRes = R.drawable.ic_person,
            readOnly = true
        )
        Spacer(Modifier.height(14.dp))
        DataFlowTextField(
            value = otp,
            onValueChange = {},
            placeholder = "OTP",
            iconRes = R.drawable.ic_mail,
            readOnly = true
        )
        Spacer(Modifier.height(14.dp))
        DataFlowTextField(
            value = password,
            onValueChange = {},
            placeholder = "Password",
            iconRes = R.drawable.ic_lock,
            isPassword = true,
            readOnly = true
        )
        ErrorText(message = confirmError)
        Spacer(Modifier.height(24.dp))
        DataFlowButton(text = "Submit", onClick = onSubmitClick)
    }
}
