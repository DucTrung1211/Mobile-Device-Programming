package com.example.tuan05project.features.todo.model

enum class TodoTab(val label: String) {
    HOME("Trang chủ"),
    LIST("Danh sách"),
    NOTIFICATIONS("Thông báo"),
    SETTINGS("Cài đặt")
}

enum class ThemeMode(val label: String) {
    LIGHT("Sáng"),
    DARK("Tối"),
    AUTO("Tự động")
}

enum class AccentColor(val label: String, val colorValue: Long) {
    BLUE("Xanh dương", 0xFF2F5BEA),
    GREEN("Xanh lá", 0xFF0F8A5F),
    ORANGE("Cam", 0xFFC2410C),
    PURPLE("Tím", 0xFF7C3AED)
}

data class TodoSettings(
    val themeMode: ThemeMode = ThemeMode.LIGHT,
    val accentColor: AccentColor = AccentColor.BLUE,
    val fontScale: Float = 1f,
    val isReminderOn: Boolean = false,
    val isSoundOn: Boolean = true,
    val isDailySummaryOn: Boolean = false
)

data class UserAccount(
    val name: String,
    val email: String,
    val isGoogleAccount: Boolean
) {
    val initials: String
        get() {
            val words = name.trim().split(" ").filter { word -> word.isNotBlank() }
            return when {
                words.isEmpty() -> "?"
                words.size == 1 -> words.first().take(2).uppercase()
                else -> "${words.first().first()}${words.last().first()}".uppercase()
            }
        }
}

data class DayProgress(
    val label: String,
    val completedCount: Int,
    val isToday: Boolean,
    val isFuture: Boolean
)
