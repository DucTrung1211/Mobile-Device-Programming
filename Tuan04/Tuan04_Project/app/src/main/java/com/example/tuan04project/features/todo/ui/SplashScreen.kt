package com.example.tuan04project.features.todo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.features.todo.model.TodoAppInfo
import com.example.tuan04project.ui.theme.TodoBlue
import kotlinx.coroutines.delay

private const val LOADING_STEPS = 10
private const val STEP_DELAY_MILLIS = 120L

@Composable
fun SplashScreen(appInfo: TodoAppInfo, onFinished: () -> Unit) {
    var loadingProgress by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        repeat(LOADING_STEPS) { step ->
            delay(STEP_DELAY_MILLIS)
            loadingProgress = (step + 1) / LOADING_STEPS.toFloat()
        }
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(TodoBlue)
            .safeDrawingPadding()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TodoAppIcon(iconSize = 84.dp, backgroundColor = Color.White, contentColor = TodoBlue)
            Spacer(Modifier.height(18.dp))
            Row {
                Text(
                    text = appInfo.brandName,
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = " ${appInfo.productName}", color = Color.White, fontSize = 30.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = appInfo.slogan,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingBar(progress = loadingProgress)
            Spacer(Modifier.height(12.dp))
            Text(
                text = appInfo.version,
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun LoadingBar(progress: Float) {
    Box(
        Modifier
            .width(84.dp)
            .height(3.dp)
            .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(50))
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress)
                .fillMaxHeight()
                .background(Color.White, RoundedCornerShape(50))
        )
    }
}
