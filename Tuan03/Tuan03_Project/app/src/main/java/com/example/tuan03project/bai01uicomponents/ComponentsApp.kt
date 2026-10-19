package com.example.tuan03project.bai01uicomponents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

val TitleBlue = Color(0xFF2196D2)
val ItemBlue = Color(0xFFBBDFFC)
val ButtonBlue = Color(0xFF1598EF)
val LightTileBlue = Color(0xFFA1BEF1)
val DarkTileBlue = Color(0xFF5086EF)

@Composable
fun ComponentsApp(onExit: () -> Unit) {
    var manHinh by rememberSaveable { mutableStateOf("intro") }
    var componentId by rememberSaveable { mutableStateOf("text") }
    val trangThaiDanhSach = rememberLazyListState()
    fun quayLai() {
        when (manHinh) {
            "detail" -> manHinh = "list"
            "list" -> manHinh = "intro"
            else -> onExit()
        }
    }
    BackHandler { quayLai() }
    // Chỉ áp dụng bảng màu này cho bài mới.
    MaterialTheme(colorScheme = lightColorScheme(
        primary = ButtonBlue, background = Color.White, surface = Color.White,
        onBackground = Color.Black, onSurface = Color.Black
    )) {
        Surface(Modifier.fillMaxSize(), color = Color.White) {
            Box(Modifier.fillMaxSize().safeDrawingPadding().imePadding()) {
                when (manHinh) {
                    "intro" -> IntroScreen(onReady = { manHinh = "list" })
                    "list" -> ComponentsListScreen(trangThaiDanhSach) {
                        componentId = it
                        manHinh = "detail"
                    }
                    "detail" -> ComponentDetailScreen(componentId, onBack = { quayLai() })
                }
            }
        }
    }
}
