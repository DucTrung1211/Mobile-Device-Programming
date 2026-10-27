package com.example.tuan05project.auth

sealed class AuthUiState {
    data object SignedOut : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val userLabel: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}
