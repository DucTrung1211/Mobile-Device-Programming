package com.example.tuan04project.features.todo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tuan04project.features.todo.data.TodoRepository
import com.example.tuan04project.features.todo.model.OnboardingPage
import com.example.tuan04project.features.todo.model.TodoAppInfo

class TodoViewModel : ViewModel() {
    val appInfo: TodoAppInfo = TodoRepository.appInfo
    val pages: List<OnboardingPage> = TodoRepository.getOnboardingPages()

    var currentPageIndex by mutableIntStateOf(0)
        private set

    val currentPage: OnboardingPage
        get() = pages[currentPageIndex]

    val isLastPage: Boolean
        get() = currentPageIndex == pages.lastIndex

    fun goToNextPage() {
        if (!isLastPage) {
            currentPageIndex++
        }
    }

    fun goToPreviousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--
        }
    }
}
