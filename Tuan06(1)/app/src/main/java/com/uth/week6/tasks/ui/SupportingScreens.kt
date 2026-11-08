package com.uth.week6.tasks.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.uth.week6.tasks.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uth.week6.tasks.features.todo.model.*
import com.uth.week6.tasks.features.todo.ui.common.*
import com.uth.week6.tasks.features.todo.ui.theme.*
import com.uth.week6.tasks.features.todo.viewmodel.*

@Composable
fun OnboardingScreen(page: Int, onPage: (Int) -> Unit, onFinish: () -> Unit) {
    val titles = listOf("Mọi việc, gọn trong một chỗ", "Chia theo Học tập và Cuộc sống", "Nhắc đúng lúc")
    val descriptions = listOf(
        "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
        "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
        "Lưu tùy chọn nhắc trên từng công việc. Thông báo nền chưa được lập lịch."
    )
    BackHandler(page > 0) { onPage(page - 1) }
    BoxWithConstraints(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
        val illustrationHeight = (maxHeight - 350.dp).coerceAtLeast(210.dp)
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(32.dp).background(Color(0xFF2F5BEA), RoundedCornerShape(8.dp))) {
                    Icon(painterResource(R.drawable.ic_todo_logo_small), null, Modifier.fillMaxSize(), tint = Color.Unspecified)
                }
                Spacer(Modifier.width(10.dp))
                Text("UTH ", fontWeight = FontWeight.Bold, fontSize = 19.sp, color = Color(0xFF1C1C1E))
                Text("Todo", Modifier.weight(1f), color = Color(0xFF2F5BEA), fontSize = 19.sp)
                TextButton(onClick = onFinish) { Text("Bỏ qua", color = Color(0xFF6B6B70)) }
            }
            Box(Modifier.fillMaxWidth().height(illustrationHeight), contentAlignment = Alignment.Center) {
                AsyncImage("file:///android_asset/onboarding_$page.png", null, Modifier.size(280.dp, 300.dp))
            }
            Text(titles[page], fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1C1C1E))
            Spacer(Modifier.height(12.dp))
            Text(descriptions[page], fontSize = 16.sp, lineHeight = 24.sp, color = Color(0xFF5C5C62))
            Row(Modifier.padding(vertical = 28.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) { index ->
                    Box(Modifier.size(if (index == page) 24.dp else 8.dp, 8.dp)
                        .background(if (index == page) Color(0xFF2F5BEA) else Color(0xFFD1D1D6), CircleShape)
                        .clickable { onPage(index) })
                }
            }
            Button(
                onClick = { if (page == 2) onFinish() else onPage(page + 1) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) { Text(if (page == 2) "Bắt đầu ngay" else "Tiếp tục", fontSize = 17.sp) }
        }
    }
}

@Composable
fun SettingsScreen(state: TodoUiState, vm: TodoViewModel, onProfile: () -> Unit) {
    val colors = TodoTheme.colors
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Cài đặt", Modifier.weight(1f), fontSize = 28.sp, fontWeight = FontWeight.Bold)
            UserAvatar("?", null, 44.dp, 17.sp, Modifier.clickable(onClick = onProfile))
        }
        TodoSectionLabel("Giao diện")
        CardBlock {
            Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ThemeMode.entries.forEach { mode ->
                    Column(Modifier.weight(1f).clickable { vm.updateThemeMode(mode) }, horizontalAlignment = Alignment.CenterHorizontally) {
                        val selected = state.settings.themeMode == mode
                        Box(Modifier.fillMaxWidth().height(80.dp).border(if (selected) 2.dp else 1.dp,
                            if (selected) colors.accent else colors.outline, RoundedCornerShape(14.dp))
                            .background(if (mode == ThemeMode.DARK) Color.Black else Color(0xFFF5F5F7), RoundedCornerShape(14.dp))
                            .padding(10.dp)) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(Modifier.size(45.dp, 7.dp).background(Color.Gray, CircleShape))
                                repeat(2) {
                                    Box(Modifier.fillMaxWidth().height(18.dp).background(
                                        if (mode == ThemeMode.DARK) Color(0xFF2A2A2D) else Color.White, RoundedCornerShape(5.dp)))
                                }
                            }
                        }
                        Text(mode.label, Modifier.padding(top = 8.dp), color = if (selected) colors.accent else colors.textSecondary, fontSize = 14.sp)
                    }
                }
            }
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = colors.divider)
            Row(Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Màu chủ đạo", Modifier.weight(1f), fontSize = 15.sp)
                AccentColor.entries.forEach { accent ->
                    Box(Modifier.padding(start = 14.dp).size(28.dp)
                        .border(if (accent == state.settings.accentColor) 2.dp else 0.dp, colors.textSecondary, CircleShape)
                        .padding(3.dp).background(Color(accent.colorValue), CircleShape)
                        .clickable { vm.updateAccentColor(accent) })
                }
            }
            HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = colors.divider)
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Cỡ chữ", Modifier.weight(1f))
                Text("A", fontSize = 12.sp)
                Slider(state.settings.fontScale, vm::updateFontScale, Modifier.width(120.dp), valueRange = 0.85f..1.2f)
                Text("A", fontSize = 18.sp)
            }
        }
        TodoSectionLabel("Thông báo")
        CardBlock {
            SettingSwitch("Nhắc việc", "Chỉ lưu remind trong từng công việc", false, vm::updateReminderEnabled)
            HorizontalDivider(color = colors.divider)
            SettingSwitch("Âm thanh", "Phát âm khi đánh dấu công việc", state.settings.isSoundOn, vm::updateSoundEnabled)
            HorizontalDivider(color = colors.divider)
            SettingSwitch("Tổng kết cuối ngày", "Chưa có bộ lập lịch nền", false, vm::updateDailySummaryEnabled)
        }
        TodoSectionLabel("Chung")
        CardBlock {
            TextButton(onClick = onProfile, modifier = Modifier.fillMaxWidth()) { Text("Hồ sơ và thống kê") }
            TextButton(onClick = vm::refresh, modifier = Modifier.fillMaxWidth(), enabled = !state.busy && !state.loading) { Text("Đồng bộ dữ liệu API") }
            Text("Ngôn ngữ: Tiếng Việt", Modifier.padding(16.dp), color = colors.textSecondary, fontSize = 14.sp)
        }
    }
}

@Composable
private fun SettingSwitch(title: String, subtitle: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp)
            Text(subtitle, color = TodoTheme.colors.textSecondary, fontSize = 12.sp)
        }
        TodoSwitch(checked, onChecked)
    }
}

@Composable
fun ProfileScreen(state: TodoUiState, onBack: () -> Unit, onRefresh: () -> Unit) {
    val colors = TodoTheme.colors
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("‹ Cài đặt") }
            Text("Hồ sơ", fontWeight = FontWeight.SemiBold)
        }
        UserAvatar("?", null, 88.dp, 32.sp)
        Text("Người dùng API bài học", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Chưa đăng nhập tài khoản cá nhân", color = colors.textSecondary, fontSize = 14.sp)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            listOf(state.tasks.count { it.isDone }.toString() to "Đã xong",
                state.pendingCount.toString() to "Cần làm",
                "${state.todayProgressPercent}%" to "Hôm nay").forEach { (value, label) ->
                Column(Modifier.weight(1f).background(colors.card, RoundedCornerShape(16.dp)).padding(12.dp)) {
                    Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(label, color = colors.textSecondary, fontSize = 12.sp)
                }
            }
        }
        CardBlock {
            Text("Dữ liệu thực từ API", Modifier.padding(16.dp), fontWeight = FontWeight.SemiBold)
            Text("API không có ngày hoàn thành hoặc lịch sử. Chưa thể tính chuỗi ngày, đúng hạn hay biểu đồ tuần một cách đáng tin cậy.",
                Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = colors.textSecondary, fontSize = 14.sp)
            TextButton(onClick = onRefresh, enabled = !state.loading && !state.busy) { Text("Đồng bộ dữ liệu") }
        }
        Text("Đăng nhập Google của Tuần 05 dùng applicationId khác. App này chưa có OAuth/Firebase riêng; không giả lập đăng nhập.",
            color = colors.textSecondary, fontSize = 13.sp)
    }
}

@Composable
fun RemindersScreen(state: TodoUiState, onOpen: (String) -> Unit) {
    val reminders = state.tasks.filter { it.isReminderOn && !it.isDone }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Thông báo", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text("Các công việc đã bật remind. Chưa có bộ lập lịch thông báo nền.", color = TodoTheme.colors.textSecondary, fontSize = 14.sp)
        }
        if (reminders.isEmpty()) item { Text("Chưa có công việc cần nhắc.") }
        items(reminders, key = { it.id }) { task ->
            CardBlock {
                Column(Modifier.fillMaxWidth().clickable { onOpen(task.id) }.padding(16.dp)) {
                    Text(task.title, fontWeight = FontWeight.Medium)
                    Text(formatDueLabel(task.date, task.time, state.today), color = TodoTheme.colors.textSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}
