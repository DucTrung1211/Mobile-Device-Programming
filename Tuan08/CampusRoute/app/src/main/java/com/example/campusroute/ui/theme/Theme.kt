package com.example.campusroute.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CampusBlueLight,
    secondary = Color(0xFF86EFAC),
    primaryContainer = Color(0xFF1E3A8A),
    background = Color(0xFF101827),
    surface = Color(0xFF182234),
    surfaceVariant = Color(0xFF202B3B),
    outlineVariant = Color(0xFF64748B)
)

private val LightColorScheme = lightColorScheme(
    primary = CampusBlue,
    secondary = StartGreen,
    primaryContainer = Color(0xFFDBEAFE),
    background = Color(0xFFF7F9FC),
    surface = Color.White,
    surfaceVariant = Color(0xFFE8EDF5),
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun CampusRouteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // A stable palette makes the grid and legend consistent across devices.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
