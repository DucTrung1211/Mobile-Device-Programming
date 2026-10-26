package com.example.tuan04project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BangMau = lightColorScheme(
    primary = XanhDuong,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun Tuan04Theme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = BangMau, typography = KieuChu, content = content)
}
