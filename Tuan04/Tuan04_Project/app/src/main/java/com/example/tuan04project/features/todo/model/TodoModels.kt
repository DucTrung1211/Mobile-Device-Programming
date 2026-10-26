package com.example.tuan04project.features.todo.model

enum class OnboardingIllustration {
    TASK_LIST,
    TASK_GROUPS,
    REMINDER
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val illustration: OnboardingIllustration,
    val buttonText: String
)

data class TodoAppInfo(
    val brandName: String,
    val productName: String,
    val slogan: String,
    val version: String
)
