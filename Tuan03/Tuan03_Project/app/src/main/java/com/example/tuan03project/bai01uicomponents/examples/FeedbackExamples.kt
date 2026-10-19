package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackExample(id: String) {
    var tienTrinh by rememberSaveable { mutableFloatStateOf(0.5f) }
    var dangTai by rememberSaveable { mutableStateOf(false) }
    val snackbar = remember { SnackbarHostState() }
    val phamVi = rememberCoroutineScope()
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        when (id) {
            "circular", "linear" -> {
                if (id == "circular") CircularProgressIndicator(progress = { tienTrinh })
                else LinearProgressIndicator(progress = { tienTrinh }, modifier = Modifier.fillMaxWidth())
                Slider(value = tienTrinh, onValueChange = { tienTrinh = it })
                Text("Tiến trình: ${(tienTrinh * 100).toInt()}%")
            }
            "snackbar" -> {
                Button(onClick = {
                    phamVi.launch { snackbar.showSnackbar("Đã nhấn nút", actionLabel = "Đóng") }
                }) { Text("Hiện Snackbar") }
                SnackbarHost(hostState = snackbar)
            }
            "tooltip" -> {
                Text("Nhấn giữ nút bên dưới để xem Tooltip")
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                    tooltip = { PlainTooltip { Text("Thêm một bài học") } },
                    state = rememberTooltipState()
                ) {
                    IconButton(onClick = { dangTai = !dangTai }) { Text("+") }
                }
                Text(if (dangTai) "Đã thêm bài học" else "Chưa thêm")
            }
            "loading" -> {
                Button(onClick = { dangTai = !dangTai }) { Text(if (dangTai) "Hoàn tất" else "Bắt đầu tải mẫu") }
                if (dangTai) {
                    CircularProgressIndicator()
                    Text("Đang tải · trạng thái minh họa")
                } else Text("Sẵn sàng")
            }
        }
    }
}
