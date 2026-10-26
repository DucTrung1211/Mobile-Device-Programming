package com.example.tuan04project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tuan04project.navigation.AppNavigation
import com.example.tuan04project.ui.theme.Tuan04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val thanhHeThong = SystemBarStyle.light(
            android.graphics.Color.TRANSPARENT,
            android.graphics.Color.TRANSPARENT
        )
        enableEdgeToEdge(statusBarStyle = thanhHeThong, navigationBarStyle = thanhHeThong)
        setContent {
            Tuan04Theme {
                AppNavigation()
            }
        }
    }
}
