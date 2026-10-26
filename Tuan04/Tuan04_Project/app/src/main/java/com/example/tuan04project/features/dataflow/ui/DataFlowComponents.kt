package com.example.tuan04project.features.dataflow.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.ui.theme.BorderGray
import com.example.tuan04project.ui.theme.SmartBlue
import com.example.tuan04project.ui.theme.SmartResultRed
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray

@Composable
fun DataFlowScreenLayout(
    title: String,
    description: String,
    onBack: (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        ) {
            if (onBack != null) {
                BackSquareButton(onClick = onBack)
            }
        }
        Spacer(Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.uth_logo),
            contentDescription = "Logo UTH",
            modifier = Modifier.size(width = 110.dp, height = 70.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "SmartTasks", color = SmartBlue, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(28.dp))
        Text(text = title, color = TextDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            color = TextGray,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        content()
    }
}

@Composable
private fun BackSquareButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .background(SmartBlue, RoundedCornerShape(10.dp))
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_chevron),
            contentDescription = "Quay lại",
            tint = Color.White,
            modifier = Modifier
                .size(26.dp)
                .graphicsLayer { rotationZ = 180f }
        )
    }
}

@Composable
fun DataFlowTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconRes: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: (() -> Unit)? = null,
    readOnly: Boolean = false
) {
    val shouldHideText = isPassword && !isPasswordVisible
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        readOnly = readOnly,
        singleLine = true,
        placeholder = { Text(text = placeholder, color = TextGray) },
        leadingIcon = {
            Icon(painter = painterResource(iconRes), contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
        },
        trailingIcon = if (onTogglePasswordVisibility != null) {
            {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        painter = painterResource(if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility),
                        contentDescription = if (isPasswordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        } else {
            null
        },
        visualTransformation = if (shouldHideText) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SmartBlue,
            unfocusedBorderColor = BorderGray,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun DataFlowButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SmartBlue)
    ) {
        Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.White)
    }
}

@Composable
fun ErrorText(message: String?) {
    if (message != null) {
        Text(
            text = message,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            color = SmartResultRed,
            fontSize = 12.sp
        )
    }
}

fun maskPassword(password: String): String {
    return "•".repeat(password.length)
}
