package com.example.tuan05project.auth

data class AuthUser(
    val uid: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
    val providers: List<String>
) {
    val displayName: String get() = name?.takeIf { it.isNotBlank() } ?: email?.substringBefore("@") ?: "Bạn"
    val providerLabel: String get() = if ("google.com" in providers) "Đã liên kết tài khoản Google" else providers.joinToString().ifBlank { "Firebase Authentication" }
}
data class AuthUiState(
    val user: AuthUser? = null,
    /** True until Firebase reports the persisted session, so a signed-in user never sees the login screen flash. */
    val restoring: Boolean = true,
    val loading: Boolean = false,
    val message: String? = null
)
