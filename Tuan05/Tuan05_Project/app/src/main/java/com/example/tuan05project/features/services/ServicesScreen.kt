package com.example.tuan05project.features.services

import android.Manifest
import android.os.Build
import android.content.pm.PackageManager
import android.app.NotificationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.tuan05project.features.todo.ui.common.TodoTopBar
import com.example.tuan05project.features.todo.viewmodel.TodoUiState
import com.example.tuan05project.messaging.ReceivedNotice
import com.google.android.gms.ads.*

@Composable
fun ServicesScreen(todo: TodoUiState, services: ServicesState, notices: List<ReceivedNotice>,
                   onBack: () -> Unit, onToken: () -> Unit, onRefresh: () -> Unit, onOpenTask: (String) -> Unit) {
    val context = LocalContext.current
    var permissionRevision by remember { mutableIntStateOf(0) }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permissionRevision++ }
    Column(Modifier.fillMaxSize().safeDrawingPadding()) {
        TodoTopBar("Dịch vụ Firebase", "Quay lại", null, onBack)
        Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Cloud Messaging / Notifications", style = MaterialTheme.typography.titleMedium)
            val granted = permissionRevision.let {
                Build.VERSION.SDK_INT < 33 || context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            }
            val enabled = context.getSystemService(NotificationManager::class.java).areNotificationsEnabled()
            Text(if (granted && enabled) "Quyền thông báo đang bật." else "Thông báo chưa được cho phép hoặc bị tắt trong Cài đặt hệ thống.")
            if (Build.VERSION.SDK_INT >= 33 && !granted) Button(onClick = { permission.launch(Manifest.permission.POST_NOTIFICATIONS) }) { Text("Cho phép thông báo") }
            Text(services.tokenStatus)
            Button(enabled = !services.working && todo.user != null, onClick = onToken) { Text("Đăng ký lại thiết bị FCM") }
            Text("Tin đã nhận thực tế trên thiết bị")
            val own = notices.filter { it.uid == todo.user?.uid }
            if (own.isEmpty()) Text("Chưa nhận được tin FCM. Cần gửi từ Console hoặc backend tin cậy.")
            own.forEach { notice ->
                TextButton(onClick = { notice.taskId?.let(onOpenTask) }) {
                    Text("Cập nhật lúc ${java.text.DateFormat.getDateTimeInstance().format(java.util.Date(notice.time))}")
                }
            }
            HorizontalDivider()
            Text("Remote Config", style = MaterialTheme.typography.titleMedium)
            Text("Giới hạn việc ở trang chủ: ${todo.taskLimit}. ${todo.configStatus}")
            OutlinedButton(onClick = onRefresh) { Text("Fetch / Activate") }
            HorizontalDivider()
            Text("Crashlytics", style = MaterialTheme.typography.titleMedium)
            Text("SDK và Gradle plugin đã tích hợp. Chỉ bản debug có nút gửi lỗi kiểm thử; cần xác nhận báo cáo trên Console.")
            DebugCrashControls()
            HorizontalDivider()
            Text("AdMob — khu vực demo riêng", style = MaterialTheme.typography.titleMedium)
            Text("Chỉ tải quảng cáo thử nghiệm chính thức khi bạn nhấn nút dưới đây.")
            TestBanner()
            Text("Hosting, Test Lab/DDP, Google Ads và các dịch vụ cũ: xem bảng đối chiếu trong README Tuần 05. Chưa triển khai Hosting, chạy test cloud hoặc quảng cáo trả phí.")
        }
    }
}
@Composable
private fun TestBanner() {
    val context = LocalContext.current
    var load by remember { mutableStateOf(false) }
    var ready by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Chưa tải quảng cáo.") }
    var adView by remember { mutableStateOf<AdView?>(null) }
    DisposableEffect(Unit) { onDispose { adView?.destroy(); adView = null } }
    Text(status)
    if (!load) OutlinedButton(onClick = {
        load = true; status = "Đang khởi tạo SDK…"
        MobileAds.initialize(context.applicationContext) { ready = true }
    }) { Text("Tải banner thử nghiệm") }
    if (ready) AndroidView(
        factory = { ctx ->
            AdView(ctx).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111"
                adListener = object : AdListener() {
                    override fun onAdLoaded() { status = "Đã tải banner thử nghiệm." }
                    override fun onAdFailedToLoad(error: LoadAdError) { status = "Tải quảng cáo lỗi (mã ${error.code})." }
                }
                adView = this
                loadAd(AdRequest.Builder().build())
            }
        }, modifier = Modifier.fillMaxWidth().height(60.dp))
}
