package com.example.tuan03project.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun Tuan03ProjectTheme(content: @Composable () -> Unit) {

    MaterialTheme(colorScheme = lightColorScheme()) {
        content()
    }
}
