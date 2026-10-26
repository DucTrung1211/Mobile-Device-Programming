package com.example.tuan04project.features.dataflow.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.features.dataflow.viewmodel.OTP_LENGTH
import com.example.tuan04project.ui.theme.SmartBlue
import com.example.tuan04project.ui.theme.SmartFieldGray
import com.example.tuan04project.ui.theme.TextDark

@Composable
fun VerifyCodeScreen(
    email: String,
    otp: String,
    otpError: String?,
    onOtpChange: (String) -> Unit,
    onBack: () -> Unit,
    onNextClick: () -> Unit
) {
    DataFlowScreenLayout(
        title = "Verify Code",
        description = "Enter the code we just sent to your registered Email\n$email",
        onBack = onBack
    ) {
        OtpInput(otp = otp, onOtpChange = onOtpChange)
        ErrorText(message = otpError)
        Spacer(Modifier.height(24.dp))
        DataFlowButton(text = "Next", onClick = onNextClick)
    }
}

@Composable
private fun OtpInput(otp: String, onOtpChange: (String) -> Unit) {
    BasicTextField(
        value = otp,
        onValueChange = onOtpChange,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Ô nhập mã xác thực" },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                repeat(OTP_LENGTH) { index ->
                    OtpDigitBox(
                        digit = otp.getOrNull(index)?.toString() ?: "",
                        isNextToFill = index == otp.length,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    )
}

@Composable
private fun OtpDigitBox(digit: String, isNextToFill: Boolean, modifier: Modifier) {
    val boxShape = RoundedCornerShape(10.dp)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(SmartFieldGray, boxShape)
            .border(1.dp, if (isNextToFill) SmartBlue else Color.Transparent, boxShape),
        contentAlignment = Alignment.Center
    ) {
        Text(text = digit, color = TextDark, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}
