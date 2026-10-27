package com.example.tuan05project.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LoginButtonColor = Color(0xFF2196F3)
private val ErrorBoxColor = Color(0x4DEB9797)
private val SuccessBoxColor = Color(0x4D4AABD2)
private val MessageTextColor = Color(0xFF333333)

private val ContentMaxWidth = 315.dp

private val MessageTextStyle = TextStyle(
    color = MessageTextColor,
    fontSize = 18.sp,
    lineHeight = 22.sp,
    letterSpacing = (-0.36).sp,
    textAlign = TextAlign.Center
)

@Composable
fun AuthScreen(uiState: AuthUiState, onLoginClick: () -> Unit) {
    val hasResult = uiState is AuthUiState.Success || uiState is AuthUiState.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        LoginButton(isLoading = uiState is AuthUiState.Loading, onClick = onLoginClick)
        when (uiState) {
            is AuthUiState.Success -> SuccessMessage(userLabel = uiState.userLabel)
            is AuthUiState.Error -> ErrorMessage(message = uiState.message)
            else -> Unit
        }
        Spacer(Modifier.weight(if (hasResult) 1.7f else 1f))
    }
}

@Composable
private fun LoginButton(isLoading: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = !isLoading,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = LoginButtonColor,
            disabledContainerColor = LoginButtonColor
        ),
        modifier = Modifier
            .widthIn(max = ContentMaxWidth)
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = "Login by Gmail",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp
            )
        }
    }
}

@Composable
private fun SuccessMessage(userLabel: String) {
    MessageBox(backgroundColor = SuccessBoxColor, minHeight = 138.dp) {
        Text(text = "Success!", style = MessageTextStyle, fontWeight = FontWeight.Bold)
        Text(text = "Hi $userLabel", style = MessageTextStyle, fontWeight = FontWeight.Bold)
        Text(
            text = buildAnnotatedString {
                append("Welcome to ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("UTHSmartTasks") }
            },
            style = MessageTextStyle
        )
    }
}

@Composable
private fun ErrorMessage(message: String) {
    MessageBox(backgroundColor = ErrorBoxColor, minHeight = 126.dp) {
        Text(text = "Google Sign-In Failed", style = MessageTextStyle, fontWeight = FontWeight.Bold)
        Text(text = message, style = MessageTextStyle)
    }
}

@Composable
private fun MessageBox(
    backgroundColor: Color,
    minHeight: Dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 18.dp)
            .widthIn(max = ContentMaxWidth)
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .background(backgroundColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 17.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
        content = content
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SignedOutPreview() {
    AuthScreen(uiState = AuthUiState.SignedOut, onLoginClick = {})
}

@Preview(showSystemUi = true)
@Composable
private fun ErrorPreview() {
    AuthScreen(
        uiState = AuthUiState.Error("User canceled the Google sign-in process."),
        onLoginClick = {}
    )
}

@Preview(showSystemUi = true)
@Composable
private fun SuccessPreview() {
    AuthScreen(uiState = AuthUiState.Success("sample@gmail.com"), onLoginClick = {})
}
