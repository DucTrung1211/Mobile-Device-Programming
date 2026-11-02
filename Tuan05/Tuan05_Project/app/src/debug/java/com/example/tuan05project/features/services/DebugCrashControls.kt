package com.example.tuan05project.features.services
import androidx.compose.runtime.*
import androidx.compose.material3.*
import com.google.firebase.crashlytics.FirebaseCrashlytics

@Composable fun DebugCrashControls() {
    var confirm by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    OutlinedButton(onClick = {
        FirebaseCrashlytics.getInstance().recordException(IllegalStateException("Week05 debug non-fatal verification"))
        message = "Đã gửi yêu cầu ghi lỗi; cần kiểm tra Console."
    }) { Text("Ghi lỗi non-fatal (debug)") }
    OutlinedButton(onClick = { confirm = true }) { Text("Kiểm thử crash (debug)") }
    message?.let { Text(it) }
    if (confirm) AlertDialog(onDismissRequest = { confirm = false }, title = { Text("Kiểm thử Crashlytics") },
        text = { Text("Ứng dụng debug sẽ đóng. Mở lại để SDK gửi báo cáo, rồi kiểm tra Console.") },
        confirmButton = { TextButton(onClick = { error("Week05 debug crash verification") }) { Text("Tạo crash") } },
        dismissButton = { TextButton(onClick = { confirm = false }) { Text("Hủy") } })
}
