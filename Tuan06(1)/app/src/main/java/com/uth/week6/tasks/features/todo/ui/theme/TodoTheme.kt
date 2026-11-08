package com.uth.week6.tasks.features.todo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.uth.week6.tasks.features.todo.model.AccentColor
import com.uth.week6.tasks.features.todo.model.TaskCategory
import com.uth.week6.tasks.features.todo.model.TaskPriority
import com.uth.week6.tasks.features.todo.model.ThemeMode
import com.uth.week6.tasks.features.todo.model.TodoSettings


data class TodoColors(
    val isDark: Boolean,
    val accent: Color,
    val accentText: Color,
    val accentSoft: Color,
    val onAccentSoft: Color,
    val background: Color,
    val card: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textDone: Color,
    val textBody: Color,
    val textLabel: Color,
    val divider: Color,
    val navBorder: Color,
    val circleBorder: Color,
    val fieldBackground: Color,
    val segmentSelected: Color,
    val valueButton: Color,
    val outline: Color,
    val chevron: Color,
    val track: Color,
    val studyChip: Color,
    val studyText: Color,
    val lifeChip: Color,
    val lifeText: Color,
    val urgentChip: Color,
    val urgentText: Color,
    val mediumChip: Color,
    val mediumText: Color,
    val successChip: Color,
    val successText: Color,
    val danger: Color,
    val switchOn: Color,
    val switchOff: Color,
    val notificationDot: Color
)

fun todoColors(isDark: Boolean, accent: Color): TodoColors {
    return if (isDark) {
        TodoColors(
            isDark = true,
            accent = accent,
            accentText = lerp(accent, Color.White, 0.4f),
            accentSoft = lerp(accent, Color.Black, 0.65f),
            onAccentSoft = lerp(accent, Color.White, 0.55f),
            background = Color(0xFF000000),
            card = Color(0xFF1C1C1E),
            textPrimary = Color(0xFFF2F2F7),
            textSecondary = Color(0xFFA1A1A8),
            textDone = Color(0xFF8E8E93),
            textBody = Color(0xFFD1D1D6),
            textLabel = Color(0xFFAEAEB2),
            divider = Color(0xFF2C2C2E),
            navBorder = Color(0xFF2C2C2E),
            circleBorder = Color(0xFF5A5A5F),
            fieldBackground = Color(0xFF1C1C1E),
            segmentSelected = Color(0xFF3A3A3C),
            valueButton = Color(0xFF2C2C2E),
            outline = Color(0xFF3A3A3C),
            chevron = Color(0xFF636366),
            track = Color(0xFF2C2C2E),
            studyChip = Color(0xFF1E2A4D),
            studyText = Color(0xFF9DB5FF),
            lifeChip = Color(0xFF3D2614),
            lifeText = Color(0xFFFFB27A),
            urgentChip = Color(0xFF3D1A17),
            urgentText = Color(0xFFFF8A80),
            mediumChip = Color(0xFF3A2F12),
            mediumText = Color(0xFFF2C14E),
            successChip = Color(0xFF12301F),
            successText = Color(0xFF5FD39A),
            danger = Color(0xFFFF6B5A),
            switchOn = Color(0xFF30D158),
            switchOff = Color(0xFF39393D),
            notificationDot = Color(0xFFFF6369)
        )
    } else {
        TodoColors(
            isDark = false,
            accent = accent,
            accentText = accent,
            accentSoft = lerp(accent, Color.White, 0.88f),
            onAccentSoft = lerp(accent, Color.Black, 0.12f),
            background = Color(0xFFF5F5F7),
            card = Color(0xFFFFFFFF),
            textPrimary = Color(0xFF1C1C1E),
            textSecondary = Color(0xFF6B6B70),
            textDone = Color(0xFF8A8A8E),
            textBody = Color(0xFF3A3A3C),
            textLabel = Color(0xFF5C5C62),
            divider = Color(0xFFEDEDF0),
            navBorder = Color(0xFFE5E5EA),
            circleBorder = Color(0xFFC7C7CC),
            fieldBackground = Color(0xFFE9E9EE),
            segmentSelected = Color(0xFFFFFFFF),
            valueButton = Color(0xFFF0F0F3),
            outline = Color(0xFFDCDCE0),
            chevron = Color(0xFFAEAEB2),
            track = Color(0xFFE4E4EA),
            studyChip = Color(0xFFE6EDFF),
            studyText = Color(0xFF1F4FD1),
            lifeChip = Color(0xFFFFEBD9),
            lifeText = Color(0xFFA24A08),
            urgentChip = Color(0xFFFDE7E7),
            urgentText = Color(0xFFB42318),
            mediumChip = Color(0xFFFFF4D6),
            mediumText = Color(0xFF9A6700),
            successChip = Color(0xFFDDF5EA),
            successText = Color(0xFF0F7A4F),
            danger = Color(0xFFC4321F),
            switchOn = Color(0xFF34C759),
            switchOff = Color(0xFFD1D1D6),
            notificationDot = Color(0xFFE5484D)
        )
    }
}

fun TodoColors.categoryChip(category: TaskCategory): Color {
    return if (category == TaskCategory.STUDY) studyChip else lifeChip
}

fun TodoColors.categoryText(category: TaskCategory): Color {
    return if (category == TaskCategory.STUDY) studyText else lifeText
}

fun TodoColors.priorityChip(priority: TaskPriority): Color {
    return when (priority) {
        TaskPriority.HIGH -> urgentChip
        TaskPriority.MEDIUM -> mediumChip
        TaskPriority.LOW -> successChip
    }
}

fun TodoColors.priorityText(priority: TaskPriority): Color {
    return when (priority) {
        TaskPriority.HIGH -> urgentText
        TaskPriority.MEDIUM -> mediumText
        TaskPriority.LOW -> successText
    }
}

fun AccentColor.toColor(): Color = Color(colorValue)

private val LocalTodoColors = staticCompositionLocalOf { todoColors(isDark = false, accent = AccentColor.BLUE.toColor()) }

object TodoTheme {
    val colors: TodoColors
        @Composable get() = LocalTodoColors.current
}

@Composable
fun TodoTheme(settings: TodoSettings, content: @Composable () -> Unit) {
    val isDark = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.AUTO -> isSystemInDarkTheme()
    }
    com.uth.week6.tasks.core.ui.SystemBarIcons(useDarkIcons = !isDark)
    val colors = todoColors(isDark, settings.accentColor.toColor())
    val baseDensity = LocalDensity.current
    val scaledDensity = Density(
        density = baseDensity.density,
        fontScale = baseDensity.fontScale * settings.fontScale
    )
    val materialColors = if (isDark) {
        darkColorScheme(
            primary = colors.accent,
            onPrimary = Color.White,
            background = colors.background,
            surface = colors.card,
            onSurface = colors.textPrimary,
            surfaceContainerHigh = colors.card
        )
    } else {
        lightColorScheme(
            primary = colors.accent,
            onPrimary = Color.White,
            background = colors.background,
            surface = colors.card,
            onSurface = colors.textPrimary,
            surfaceContainerHigh = colors.card
        )
    }


    CompositionLocalProvider(
        LocalTodoColors provides colors,
        LocalDensity provides scaledDensity
    ) {
        MaterialTheme(colorScheme = materialColors, typography = TodoTypography, content = content)
    }
}
