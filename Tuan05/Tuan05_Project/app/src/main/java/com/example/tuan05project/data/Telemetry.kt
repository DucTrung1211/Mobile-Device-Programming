package com.example.tuan05project.data

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.tasks.await

interface Telemetry {
    fun event(name: String, parameter: String? = null, value: String? = null)
}
class FirebaseTelemetry(context: Context) : Telemetry {
    private val analytics = FirebaseAnalytics.getInstance(context.applicationContext)
    override fun event(name: String, parameter: String?, value: String?) {
        val params = Bundle()
        if (parameter != null && value != null) params.putString(parameter, value)
        analytics.logEvent(name, params)
    }
    fun screen(name: String) = event(FirebaseAnalytics.Event.SCREEN_VIEW, FirebaseAnalytics.Param.SCREEN_NAME, name)
}
class RemoteConfigRepository {
    private val config get() = FirebaseRemoteConfig.getInstance()
    suspend fun refresh(): Pair<Int, Boolean> {
        config.setConfigSettingsAsync(FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()).await()
        config.setDefaultsAsync(mapOf("home_task_limit" to 4L)).await()
        val activated = config.fetchAndActivate().await()
        return config.getLong("home_task_limit").toInt().coerceIn(1, 10) to activated
    }
}
