package com.example.tuan05project.features.todo.ui.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.tuan05project.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan05project.features.todo.ui.common.*
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import com.example.tuan05project.features.todo.viewmodel.TodoUiState
import com.example.tuan05project.features.todo.viewmodel.percent
import com.example.tuan05project.features.todo.model.DayProgress

@Composable
fun ProfileScreen(state: TodoUiState, onBack: () -> Unit, onLogout: () -> Unit,
                  onUpload: (Uri) -> Unit, onRename: (String) -> Unit) {
    val colors = TodoTheme.colors
    var confirmLogout by remember { mutableStateOf(false) }
    var rename by remember { mutableStateOf(false) }
    var privacy by remember { mutableStateOf(false) }
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> uri?.let(onUpload) }
    val completed = state.tasks.count { it.isDone }
    val days = (0..6).map { offset ->
        val date = state.today.with(java.time.DayOfWeek.MONDAY).plusDays(offset.toLong())
        DayProgress(if (offset == 6) "CN" else "T${offset + 2}", state.tasks.count { it.completedDate == date },
            date == state.today, date > state.today)
    }
    Column(Modifier.fillMaxSize().background(colors.background).safeDrawingPadding().verticalScroll(rememberScrollState())) {
        TodoTopBar("Hồ sơ", "Cài đặt", if (state.user != null) "Sửa" else null, onBack, { rename = true })
        Column(Modifier.padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(Modifier.height(4.dp))
            Box {
                UserAvatar(state.account?.initials ?: "K", state.avatarUri, 88.dp, 32.sp,
                    Modifier.clickable(enabled = state.integrated && state.user != null && state.uploadProgress == null) { picker.launch("image/*") })
                Box(Modifier.align(Alignment.BottomEnd).offset(x = 4.dp, y = 4.dp).size(34.dp)
                    .clip(CircleShape).background(colors.accent)
                    .clickable(enabled = state.integrated && state.user != null && state.uploadProgress == null) { picker.launch("image/*") },
                    contentAlignment = Alignment.Center) {
                    Icon(painterResource(R.drawable.ic_todo_camera), "Đổi ảnh đại diện", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(16.dp))
                }
            }
            if (state.uploadProgress != null) {
                LinearProgressIndicator(progress = { state.uploadProgress }, modifier = Modifier.fillMaxWidth())
                Text("Đang tải ảnh… ${(state.uploadProgress * 100).toInt()}%", color = colors.textSecondary)
            }
            Text(state.user?.displayName ?: "Phiên khách", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Text(state.user?.email?.takeIf { it.isNotBlank() } ?: "Không có email", color = colors.textSecondary, fontSize = 14.sp)
            Row(Modifier.background(if (state.user != null) colors.successChip else colors.valueButton, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (state.account?.isGoogleAccount == true) Icon(painterResource(R.drawable.ic_check), null, tint = colors.successText, modifier = Modifier.size(13.dp))
                Text(state.user?.providerLabel ?: "Khách — chưa liên kết Google",
                    color = if (state.user != null) colors.successText else colors.textSecondary, fontSize = 12.sp)
            }
            if (!state.integrated) Text("Bài 01: thông tin tài khoản thật. Thống kê công việc được kết nối ở Bài 02.", color = colors.textSecondary, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatCard(completed.toString(), "Việc đã xong", Modifier.weight(1f))
                StatCard(state.pendingCount.toString(), "Đang chờ", Modifier.weight(1f))
                StatCard("${percent(completed, state.tasks.size)}%", "Hoàn thành", Modifier.weight(1f))
            }
            WeekCard(days, days.sumOf { it.completedCount }, state.tasks.count {
                it.date >= state.today.with(java.time.DayOfWeek.MONDAY) &&
                    it.date <= state.today.with(java.time.DayOfWeek.SUNDAY)
            })
            Column(Modifier.fillMaxWidth().background(colors.card, RoundedCornerShape(16.dp))) {
                Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(painterResource(R.drawable.ic_todo_refresh), null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
                    Text("Đồng bộ dữ liệu", Modifier.weight(1f), color = colors.textPrimary, fontSize = 15.sp)
                    Text(if (!state.integrated) "Bài 02" else if (state.loading) "Đang tải" else if (state.error != null) "Chưa sẵn sàng" else "Đang nghe",
                        color = colors.textSecondary, fontSize = 13.sp)
                }
                HorizontalDivider(color = colors.divider)
                Row(Modifier.fillMaxWidth().clickable { privacy = true }.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(painterResource(R.drawable.ic_todo_lock), null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
                    Text("Quyền riêng tư", Modifier.weight(1f), color = colors.textPrimary, fontSize = 15.sp)
                    Icon(painterResource(R.drawable.ic_todo_chevron_right), null, tint = colors.textSecondary, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.height(24.dp))
            TextButton(onClick = { confirmLogout = true }, modifier = Modifier.fillMaxWidth().height(52.dp)
                .background(colors.card, RoundedCornerShape(16.dp))) {
                    Icon(painterResource(R.drawable.ic_todo_logout), null, tint = colors.danger, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Đăng xuất", color = colors.danger, fontWeight = FontWeight.SemiBold)
                }
            Spacer(Modifier.height(20.dp))
        }
    }
    if (rename) TodoTextInputDialog("Sửa tên hiển thị", state.user?.name.orEmpty(), "Tên của bạn",
        { name -> if (name.isNotBlank() && name.length <= 100) { onRename(name.trim()); rename = false } }, { rename = false })
    if (privacy) TodoInfoDialog("Quyền riêng tư",
        "Firebase Authentication lưu phiên đăng nhập. Bài 02 lưu công việc, ảnh và token thông báo theo UID; Analytics chỉ ghi tên sự kiện và nhóm công việc. Đăng xuất để kết thúc phiên trên thiết bị này.",
        { privacy = false })
    if (confirmLogout) TodoConfirmDialog("Đăng xuất?", "Bạn sẽ quay lại màn hình đăng nhập.", "Đăng xuất",
        { confirmLogout = false; onLogout() }, { confirmLogout = false })
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    val colors = TodoTheme.colors
    Column(
        modifier = modifier
            .background(colors.card, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = value, color = colors.textPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(text = label, color = colors.textSecondary, fontSize = 12.sp, maxLines = 1)
    }
}

@Composable
private fun WeekCard(days: List<DayProgress>, completedCount: Int, totalCount: Int) {
    val colors = TodoTheme.colors
    val maxCount = maxOf(1, days.maxOf { day -> day.completedCount })
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.card, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Tuần này",
                modifier = Modifier.weight(1f),
                color = colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = "$completedCount/$totalCount việc", color = colors.textSecondary, fontSize = 13.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { day ->
                val barHeight = 16 + 48 * day.completedCount / maxCount
                val barColor = when {
                    day.isToday -> colors.accent
                    day.isFuture || day.completedCount == 0 -> colors.track
                    else -> colors.accent.copy(alpha = 0.35f)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Bottom)
                ) {
                    Box(
                        modifier = Modifier
                            .width(26.dp)
                            .height(barHeight.dp)
                            .background(barColor, RoundedCornerShape(6.dp))
                    )
                    Text(
                        text = day.label,
                        color = if (day.isToday) colors.textPrimary else colors.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
