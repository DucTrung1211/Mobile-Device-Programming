package com.example.tuan05project

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.tuan05project.auth.AuthScreen
import com.example.tuan05project.auth.AuthViewModel

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val whiteSystemBars = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = whiteSystemBars, navigationBarStyle = whiteSystemBars)

        setContent {
            MaterialTheme {
                val uiState by authViewModel.uiState.collectAsState()
                AuthScreen(
                    uiState = uiState,
                    onLoginClick = { authViewModel.signInWithGoogle(this) }
                )
            }
        }
    }
}
