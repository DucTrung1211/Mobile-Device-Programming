package com.uth.week6.permissions.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import com.uth.week6.permissions.data.PermissionRepository
import com.uth.week6.permissions.model.PermissionStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PermissionUiState(
    val step: Int,
    val labels: List<String>,
    val granted: List<Boolean>,
    val message: String? = null,
    val requesting: Boolean = false
)

class PermissionViewModel(application: Application, private val saved: SavedStateHandle) : AndroidViewModel(application) {
    private val repository = PermissionRepository(application)
    private val mutableState = MutableStateFlow(PermissionUiState(
        saved.get<Int>("step") ?: repository.savedStep(),
        PermissionStep.entries.map(repository::label),
        PermissionStep.entries.map(repository::granted),
        requesting = saved.get<Int>("pending") != null
    ))
    val state = mutableState.asStateFlow()
    val pending: Int? get() = saved["pending"]

    init { repository.createChannel() }

    fun refresh() {
        mutableState.update { it.copy(
            labels = PermissionStep.entries.map(repository::label),
            granted = PermissionStep.entries.map(repository::granted)
        ) }
    }
    fun alreadyGranted(step: PermissionStep) = repository.granted(step)
    fun asked(step: PermissionStep) = repository.asked(step)

    fun requesting(index: Int) {
        saved["pending"] = index
        repository.markAsked(PermissionStep.entries[index])
        mutableState.update { it.copy(requesting = true) }
    }
    fun result() {
        val index = pending ?: state.value.step
        saved.remove<Int>("pending")
        refresh()
        mutableState.update { it.copy(requesting = false,
            message = if (repository.granted(PermissionStep.entries[index])) "Permission allowed." else "Permission denied. You can continue and change it later in Settings.") }
        next()
    }
    fun next() {
        if (state.value.requesting) return
        val next = if (saved.get<Boolean>("review") == true) 3 else (state.value.step + 1).coerceAtMost(3)
        saved["review"] = false
        setStep(next)
    }
    fun review(index: Int) {
        saved["review"] = true
        setStep(index)
    }
    fun back() {
        if (state.value.requesting) return
        if (saved.get<Boolean>("review") == true) {
            saved["review"] = false
            setStep(3)
        } else setStep((state.value.step - 1).coerceAtLeast(0))
    }
    private fun setStep(index: Int) {
        saved["step"] = index
        repository.saveStep(index)
        mutableState.update { it.copy(step = index) }
    }
    fun testNotification() {
        val result = repository.sendTest()
        refresh()
        mutableState.update { it.copy(message = result) }
    }
    fun messageShown() = mutableState.update { it.copy(message = null) }
}
