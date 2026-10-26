package com.example.tuan04project.features.dataflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.features.dataflow.viewmodel.PasswordResetResult
import com.example.tuan04project.ui.theme.SmartResultBox
import com.example.tuan04project.ui.theme.SmartResultRed
import com.example.tuan04project.ui.theme.TextGray

@Composable
fun ForgotPasswordScreen(
    email: String,
    emailError: String?,
    submittedResult: PasswordResetResult?,
    onEmailChange: (String) -> Unit,
    onNextClick: () -> Unit
) {
    DataFlowScreenLayout(
        title = "Forgot Password?",
        description = "Enter your Email, we will send you a verification code.",
        onBack = null
    ) {
        DataFlowTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = "Your Email",
            iconRes = R.drawable.ic_mail,
            keyboardType = KeyboardType.Email
        )
        ErrorText(message = emailError)
        Spacer(Modifier.height(24.dp))
        DataFlowButton(text = "Next", onClick = onNextClick)
        Spacer(Modifier.height(32.dp))
        if (submittedResult != null) {
            SubmittedResultBox(result = submittedResult)
        }
    }
}

@Composable
private fun SubmittedResultBox(result: PasswordResetResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SmartResultBox, RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = "Data from Confirm screen", color = TextGray, fontSize = 12.sp)
        ResultLine(label = "email", value = result.email)
        ResultLine(label = "otp", value = result.otp)
        ResultLine(label = "pass", value = maskPassword(result.password))
    }
}

@Composable
private fun ResultLine(label: String, value: String) {
    Text(
        text = "$label: $value",
        color = SmartResultRed,
        fontSize = 17.sp,
        fontWeight = FontWeight.Medium
    )
}
