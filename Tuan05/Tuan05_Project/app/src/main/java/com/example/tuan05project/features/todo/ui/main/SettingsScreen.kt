package com.example.tuan05project.features.todo.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan05project.R

import com.example.tuan05project.features.todo.model.AccentColor
import com.example.tuan05project.features.todo.model.ThemeMode
import com.example.tuan05project.features.todo.ui.common.TodoInfoDialog
import com.example.tuan05project.features.todo.ui.common.TodoSectionLabel
import com.example.tuan05project.features.todo.ui.common.TodoSwitch
import com.example.tuan05project.features.todo.ui.common.UserAvatar
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import com.example.tuan05project.features.todo.ui.theme.toColor
import com.example.tuan05project.features.todo.viewmodel.TodoUiState
import com.example.tuan05project.features.todo.viewmodel.TodoActions

@Composable
fun SettingsScreen(state: TodoUiState, actions: TodoActions, onOpenProfile: () -> Unit) {
    val colors = TodoTheme.colors
    val settings = state.settings
    var isLanguageDialogVisible by remember { mutableStateOf(false) }
    var isAboutDialogVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()

            .verticalScroll(rememberScrollState())
            .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cài đặt",
                modifier = Modifier.weight(1f),
                color = colors.textPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp
            )
            UserAvatar(
                initials = state.account?.initials ?: "?",
                avatarUri = state.avatarUri,
                size = 44.dp,
                fontSize = 15.sp,
                modifier = Modifier.clickable(onClick = onOpenProfile)
            )
        }

        TodoSectionLabel(text = "Giao diện")
        SettingsCard(modifier = Modifier.padding(16.dp), spacing = 16) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ThemeMode.entries.forEach { mode ->
                    ThemeOption(
                        mode = mode,
                        isSelected = settings.themeMode == mode,
                        onClick = { actions.updateThemeMode(mode) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            HorizontalDivider(color = colors.divider)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Màu chủ đạo", modifier = Modifier.weight(1f), color = colors.textPrimary, fontSize = 15.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AccentColor.entries.forEach { accentColor ->
                        AccentSwatch(
                            accentColor = accentColor,
                            isSelected = settings.accentColor == accentColor,
                            onClick = { actions.updateAccentColor(accentColor) }
                        )
                    }
                }
            }
            HorizontalDivider(color = colors.divider)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Cỡ chữ", modifier = Modifier.weight(1f), color = colors.textPrimary, fontSize = 15.sp)
                Text(text = "A", color = colors.textSecondary, fontSize = 12.sp)
                FontSizeSlider(
                    value = settings.fontScale,
                    onValueChange = { actions.updateFontScale(it) }
                )
                Text(text = "A", color = colors.textSecondary, fontSize = 18.sp)
            }
        }

        TodoSectionLabel(text = "Thông báo", modifier = Modifier.padding(top = 6.dp))
        SettingsCard {
            SwitchRow(
                title = "Nhắc việc",
                subtitle = "Chưa có lịch nhắc tự động",
                checked = settings.isReminderOn,
                onCheckedChange = { actions.updateReminderEnabled(it) }
            )
            HorizontalDivider(color = colors.divider)
            SwitchRow(
                title = "Âm thanh",
                subtitle = "Âm thanh khi đánh dấu công việc",
                checked = settings.isSoundOn,
                onCheckedChange = { actions.updateSoundEnabled(it) }
            )
            HorizontalDivider(color = colors.divider)
            SwitchRow(
                title = "Tổng kết cuối ngày",
                subtitle = "Chưa có lịch gửi tự động",
                checked = settings.isDailySummaryOn,
                onCheckedChange = { actions.updateDailySummaryEnabled(it) }
            )
        }

        TodoSectionLabel(text = "Chung", modifier = Modifier.padding(top = 6.dp))
        SettingsCard {
            NavigationRow(title = "Ngôn ngữ", value = "Tiếng Việt", onClick = { isLanguageDialogVisible = true })
            HorizontalDivider(color = colors.divider)
            NavigationRow(
                title = "Giới thiệu UTH Todo",
                value = "1.0",
                onClick = { isAboutDialogVisible = true }
            )
        }
    }

    if (isLanguageDialogVisible) {
        TodoInfoDialog(
            title = "Ngôn ngữ",
            text = "UTH Todo hiện dùng Tiếng Việt. Các ngôn ngữ khác sẽ được bổ sung ở phiên bản sau.",
            onDismiss = { isLanguageDialogVisible = false }
        )
    }
    if (isAboutDialogVisible) {

        TodoInfoDialog(
            title = "UTH Todo",
            text = "Bài tập Tuần 05 – Firebase. Dữ liệu công việc và ảnh ở Bài 02 được lưu theo tài khoản Firebase.",
            onDismiss = { isAboutDialogVisible = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FontSizeSlider(value: Float, onValueChange: (Float) -> Unit) {
    val colors = TodoTheme.colors
    val minScale = 0.85f
    val maxScale = 1.15f
    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = minScale..maxScale,
        steps = 5,
        modifier = Modifier
            .width(120.dp)
            .padding(horizontal = 6.dp),
        thumb = {
            Box(
                Modifier
                    .size(18.dp)
                    .shadow(2.dp, CircleShape)
                    .background(Color.White, CircleShape)
            )
        },
        track = {
            val fraction = (value - minScale) / (maxScale - minScale)
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(colors.divider, RoundedCornerShape(2.dp))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .background(colors.accent, RoundedCornerShape(2.dp))
                )
            }
        }
    )
}

@Composable
private fun SettingsCard(
    modifier: Modifier = Modifier,
    spacing: Int = 0,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TodoTheme.colors.card, RoundedCornerShape(18.dp))
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(spacing.dp)
    ) {
        content()
    }
}

@Composable
private fun ThemeOption(mode: ThemeMode, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = TodoTheme.colors
    val previewShape = RoundedCornerShape(14.dp)
    val previewBackground = when (mode) {
        ThemeMode.LIGHT -> Brush.horizontalGradient(listOf(Color(0xFFF5F5F7), Color(0xFFF5F5F7)))
        ThemeMode.DARK -> Brush.horizontalGradient(listOf(Color.Black, Color.Black))
        ThemeMode.AUTO -> Brush.horizontalGradient(0.5f to Color(0xFFF5F5F7), 0.5f to Color.Black)
    }
    val barColor = when (mode) {
        ThemeMode.LIGHT -> Color(0xFFD1D1D6)
        ThemeMode.DARK -> Color(0xFF48484A)
        ThemeMode.AUTO -> Color(0xFF8E8E93)
    }
    val rowBackground = when (mode) {
        ThemeMode.LIGHT -> Brush.horizontalGradient(listOf(Color.White, Color.White))
        ThemeMode.DARK -> Brush.horizontalGradient(listOf(Color(0xFF2C2C2E), Color(0xFF2C2C2E)))
        ThemeMode.AUTO -> Brush.horizontalGradient(0.5f to Color.White, 0.5f to Color(0xFF2C2C2E))
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(84.dp)
                .clip(previewShape)
                .background(previewBackground)
                .border(2.dp, if (isSelected) colors.accent else colors.divider, previewShape)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                Modifier
                    .size(width = 45.dp, height = 8.dp)
                    .background(barColor, RoundedCornerShape(4.dp))
            )
            repeat(2) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(rowBackground)
                )
            }
        }
        Text(
            text = mode.label,
            color = if (isSelected) colors.accentText else colors.textSecondary,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@Composable
private fun AccentSwatch(accentColor: AccentColor, isSelected: Boolean, onClick: () -> Unit) {
    val color = accentColor.toColor()
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .semantics { contentDescription = "Màu ${accentColor.label}" },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .border(2.dp, color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.size(26.dp).background(color, CircleShape))
            }
        } else {
            Box(Modifier.size(26.dp).background(color, CircleShape))
        }
    }
}

@Composable
private fun SwitchRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val colors = TodoTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = title, color = colors.textPrimary, fontSize = 15.sp)
            Text(text = subtitle, color = colors.textSecondary, fontSize = 12.sp)
        }
        TodoSwitch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun NavigationRow(title: String, value: String, onClick: () -> Unit) {
    val colors = TodoTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(1f), color = colors.textPrimary, fontSize = 15.sp)
        Text(text = value, color = colors.textSecondary, fontSize = 15.sp)
        Spacer(Modifier.width(6.dp))
        Icon(
            painter = painterResource(R.drawable.ic_todo_chevron_right),
            contentDescription = null,
            tint = colors.textSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}
