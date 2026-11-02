package com.example.tuan05project.auth

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleSignInManager {
    suspend fun requestGoogleIdToken(activity: Activity): String {
        val resource = activity.resources.getIdentifier("default_web_client_id", "string", activity.packageName)
        check(resource != 0) { "Thiếu Web OAuth client ID. Cập nhật google-services.json sau khi bật Google provider." }
        val clientId = activity.getString(resource)
        check(clientId.isNotBlank()) { "Web OAuth client ID trống." }
        val option = GetSignInWithGoogleOption.Builder(clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
        val credential = CredentialManager.create(activity).getCredential(activity, request).credential
        check(credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            "Google không trả về ID token hợp lệ."
        }
        return GoogleIdTokenCredential.createFrom(credential.data).idToken
    }
    suspend fun clear(context: Context) {
        CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
    }
}
