package com.example.tuan02project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan02project.ui.theme.Tuan02_ProjectTheme

private const val START_SCREEN = 1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightSystemBar = SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = lightSystemBar,
            navigationBarStyle = lightSystemBar
        )
        setContent {
            Tuan02_ProjectTheme {
                if (START_SCREEN == 0) {
                    ThucHanh02Screen()
                } else if (START_SCREEN == 1) {
                    ThucHanh03Screen()
                } else if (START_SCREEN == 2) {
                    Bai1NullableScreen()
                } else if (START_SCREEN == 3) {
                    Bai2IfElseScreen()
                } else {
                    Bai3ForIfNullableScreen()
                }
            }
        }
    }
}

@Composable
fun ExerciseHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        modifier = Modifier.fillMaxWidth().background(Color(0xFF304776)).padding(16.dp)
    )
}
