package com.uth.week6.tasks.core.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun SystemBarIcons(useDarkIcons: Boolean) {
    val view = LocalView.current
    LaunchedEffect(useDarkIcons) {
        val activity = view.context as? Activity ?: return@LaunchedEffect
        val controller = WindowCompat.getInsetsController(activity.window, view)
        controller.isAppearanceLightStatusBars = useDarkIcons
        controller.isAppearanceLightNavigationBars = useDarkIcons
    }
}
