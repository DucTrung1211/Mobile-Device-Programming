package com.example.tuan03project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tuan03project.ui.theme.Tuan03ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightSystemBars = SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT
        )
        enableEdgeToEdge(
            statusBarStyle = lightSystemBars,
            navigationBarStyle = lightSystemBars
        )
        setContent {
            Tuan03ProjectTheme {
                CourseworkApp()
            }
        }
    }
}
