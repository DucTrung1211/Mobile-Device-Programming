package com.example.tuan05project.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {

    private val auth = Firebase.auth

    suspend fun signInWithGoogle(idToken: String): FirebaseUser {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(firebaseCredential).await()
        return result.user ?: throw IllegalStateException("Firebase did not return a user.")
    }
}
