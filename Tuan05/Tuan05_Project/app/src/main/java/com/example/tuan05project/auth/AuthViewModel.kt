package com.example.tuan05project.auth

import android.app.Activity
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val googleSignInManager = GoogleSignInManager()
    private val authRepository = FirebaseAuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.SignedOut)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun signInWithGoogle(activity: Activity) {
        if (_uiState.value == AuthUiState.Loading) return
        _uiState.value = AuthUiState.Loading

        viewModelScope.launch {
            _uiState.value = try {
                val idToken = googleSignInManager.requestGoogleIdToken(activity)
                val user = authRepository.signInWithGoogle(idToken)
                AuthUiState.Success(user.email ?: user.displayName.orEmpty())
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                AuthUiState.Error(errorMessageFor(exception))
            }
        }
    }

    private fun errorMessageFor(exception: Exception): String = when (exception) {
        is GetCredentialCancellationException -> "User canceled the Google sign-in process."
        is NoCredentialException -> "No Google account is available on this device."
        is GetCredentialException -> "Google could not sign you in. Check the app's SHA-1 and Web Client ID in Firebase."
        is FirebaseNetworkException -> "No internet connection. Please try again."
        is FirebaseAuthException -> "Firebase Authentication rejected the Google account. Make sure the Google provider is enabled."
        else -> "Something went wrong. Please try again."
    }
}
