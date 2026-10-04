package com.example.tuan01profile

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tuan01profile.ui.ProfileScreen
import com.example.tuan01profile.ui.theme.Tuan01_ProfileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.WHITE, Color.DKGRAY)
        )
        setContent {
            Tuan01_ProfileTheme(darkTheme = false, dynamicColor = false) {
                ProfileScreen(onBackClick = { finish() })
            }
        }
    }
}
