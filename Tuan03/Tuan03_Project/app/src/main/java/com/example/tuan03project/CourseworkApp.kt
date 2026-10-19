package com.example.tuan03project

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tuan03project.bai01uicomponents.ComponentsApp

@Composable
fun CourseworkApp() {
    var baiDangMo by rememberSaveable { mutableStateOf("menu") }
    BackHandler(enabled = baiDangMo == "cu") { baiDangMo = "menu" }
    when (baiDangMo) {
        "cu" -> MainScreen()
        "moi" -> ComponentsApp(onExit = { baiDangMo = "menu" })
        else -> Column(
            modifier = Modifier.fillMaxSize().background(Color.White)
                .safeDrawingPadding().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bài tập Compose", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(24.dp))
            Button(onClick = { baiDangMo = "moi" }, modifier = Modifier.fillMaxWidth()) {
                Text("Tuần 04 · Bài 01 — UI Components")
            }
            OutlinedButton(onClick = { baiDangMo = "cu" }, modifier = Modifier.fillMaxWidth()) {
                Text("Tuần 03 — My First App")
            }
        }
    }
}
