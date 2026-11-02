package com.example.tuan05project.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    val users: Flow<AuthUser?>
    suspend fun signInWithGoogle(idToken: String): AuthUser
    suspend fun rename(name: String)
    fun signOut()
}
class FirebaseAuthRepository : AuthRepository {
    private val auth get() = FirebaseAuth.getInstance()
    override val users: Flow<AuthUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { trySend(it.currentUser?.toAuthUser()) }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }
    override suspend fun signInWithGoogle(idToken: String): AuthUser {
        require(idToken.isNotBlank())
        val result = auth.signInWithCredential(GoogleAuthProvider.getCredential(idToken, null)).await()
        return checkNotNull(result.user) { "Firebase không trả về người dùng." }.toAuthUser()
    }
    override suspend fun rename(name: String) {
        require(name.isNotBlank() && name.length <= 100)
        checkNotNull(auth.currentUser).updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder().setDisplayName(name).build()).await()
    }
    override fun signOut() = auth.signOut()
}
private fun FirebaseUser.toAuthUser() = AuthUser(uid, displayName, email, photoUrl?.toString(),
    providerData.map { it.providerId }.filter { it != "firebase" })
