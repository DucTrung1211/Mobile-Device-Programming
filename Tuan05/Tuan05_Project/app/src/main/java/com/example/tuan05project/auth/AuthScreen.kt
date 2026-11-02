package com.example.tuan05project.auth

import androidx.compose.runtime.Composable
import com.example.tuan05project.features.todo.ui.account.LoginScreen

@Composable
fun AuthScreen(uiState: AuthUiState, onLoginClick: () -> Unit, onBack: () -> Unit, onGuest: () -> Unit) {
    LoginScreen(uiState.loading, onBack, onLoginClick, onGuest)
}
