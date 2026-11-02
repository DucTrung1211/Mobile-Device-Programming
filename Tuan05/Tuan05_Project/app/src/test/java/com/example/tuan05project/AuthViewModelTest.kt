package com.example.tuan05project

import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.tuan05project.auth.*
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @get:Rule val main = MainDispatcherRule()
    private val user = AuthUser("alice", null, "alice@example.test", null, listOf("google.com"))
    private class FakeAuth(initial: AuthUser? = null) : AuthRepository {
        override val users = MutableStateFlow(initial)
        var calls = 0
        var signOuts = 0
        val result = CompletableDeferred<AuthUser>()
        override suspend fun signInWithGoogle(idToken: String): AuthUser { calls++; return result.await() }
        override suspend fun rename(name: String) { users.value = users.value?.copy(name = name) }
        override fun signOut() { signOuts++; users.value = null }
    }
    @Test fun onlyFirebaseConfirmationCanSetSuccessAndDuplicateClicksAreIgnored() = runTest {
        val repo = FakeAuth(); val vm = AuthViewModel(repo)
        runCurrent()
        vm.signIn({ "google-token" }); vm.signIn({ "duplicate" })
        runCurrent()
        assertTrue(vm.uiState.value.loading); assertNull(vm.uiState.value.user); assertEquals(1, repo.calls)
        repo.result.complete(user); runCurrent()
        assertEquals(user, vm.uiState.value.user); assertFalse(vm.uiState.value.loading)
        assertEquals("Đăng nhập thành công.", vm.uiState.value.message)
    }
    @Test fun cancellationIsFeedbackWithoutFirebaseSuccess() = runTest {
        val repo = FakeAuth(); val vm = AuthViewModel(repo); runCurrent()
        vm.signIn({ throw GetCredentialCancellationException() }); runCurrent()
        assertNull(vm.uiState.value.user); assertFalse(vm.uiState.value.loading)
        assertTrue(vm.uiState.value.message!!.contains("hủy")); assertEquals(0, repo.calls)
    }
    @Test fun missingConfigurationAndNetworkAreDifferent() {
        assertTrue(authErrorMessage(IllegalStateException("Thiếu Web client ID")).contains("Web client"))
        assertTrue(authErrorMessage(FirebaseNetworkException("offline")).contains("mạng"))
    }
    @Test fun restoredSessionAndLogoutStillClearCredentialsWhenTokenCleanupFails() = runTest {
        val repo = FakeAuth(user); val vm = AuthViewModel(repo); runCurrent()
        assertEquals(user, vm.uiState.value.user)
        var cleared = false
        vm.signOut(clearCredential = { cleared = true }, beforeSignOut = { error("offline") }); runCurrent()
        assertNull(vm.uiState.value.user); assertTrue(cleared); assertEquals(1, repo.signOuts)
        assertFalse(vm.uiState.value.loading)
    }
}
