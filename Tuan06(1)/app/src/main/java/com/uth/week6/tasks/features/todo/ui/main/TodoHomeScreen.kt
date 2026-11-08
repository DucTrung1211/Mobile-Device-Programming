package com.uth.week6.tasks.features.todo.ui.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.week6.tasks.R
import com.uth.week6.tasks.features.todo.model.TaskCategory
import com.uth.week6.tasks.features.todo.model.TodoTab
import com.uth.week6.tasks.features.todo.ui.common.TaskRow
import com.uth.week6.tasks.features.todo.ui.common.UserAvatar
import com.uth.week6.tasks.features.todo.ui.common.formatHeaderDate
import com.uth.week6.tasks.features.todo.ui.common.formatTime
import com.uth.week6.tasks.features.todo.ui.common.rememberTaskSound
import com.uth.week6.tasks.features.todo.ui.theme.TodoTheme
import com.uth.week6.tasks.features.todo.ui.theme.categoryChip
import com.uth.week6.tasks.features.todo.ui.theme.categoryText
import com.uth.week6.tasks.features.todo.viewmodel.TodoUiState
import com.uth.week6.tasks.features.todo.viewmodel.TodoActions

@Composable
fun TodoHomeScreen(state: TodoUiState, actions: TodoActions, onOpenTask: (String) -> Unit, onOpenProfile: () -> Unit) {
    val colors = TodoTheme.colors
    val playSound = rememberTaskSound(state.settings.isSoundOn)
    val todayTasks = state.todayTasks

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            ,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                HomeHeader(state = state, actions = actions, onOpenProfile = onOpenProfile)
                ProgressCard(
                    doneCount = state.todayDoneCount,
                    totalCount = todayTasks.size,
                    percent = state.todayProgressPercent
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CategoryCard(
                        category = TaskCategory.STUDY,
                        iconRes = R.drawable.ic_todo_cap,
                        remainingCount = state.remainingCount(TaskCategory.STUDY),
                        onClick = { actions.showTasksOfCategory(TaskCategory.STUDY) },
                        modifier = Modifier.weight(1f)
                    )
                    CategoryCard(
                        category = TaskCategory.LIFE,
                        iconRes = R.drawable.ic_todo_house,
                        remainingCount = state.remainingCount(TaskCategory.LIFE),
                        onClick = { actions.showTasksOfCategory(TaskCategory.LIFE) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Việc hôm nay",
                        modifier = Modifier.weight(1f),
                        color = colors.textPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Xem tất cả",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { actions.showTasksOfCategory(null) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        color = colors.accentText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        if (todayTasks.isEmpty()) {
            item {
                Text(
                    text = "Hôm nay chưa có việc nào. Nhấn + để thêm việc mới.",
                    color = colors.textSecondary,
                    fontSize = 14.sp
                )
            }
        }
        items(todayTasks.take(state.taskLimit), key = { task -> task.id }) { task ->
            TaskRow(
                task = task,
                timeLabel = formatTime(task.time),
                showUrgentBadge = false,
                showChevron = true,
                onToggleDone = {
                    playSound()
                    actions.toggleTaskDone(task.id)
                },
                onClick = { onOpenTask(task.id) }
            )
        }
    }
}

@Composable
private fun HomeHeader(state: TodoUiState, actions: TodoActions, onOpenProfile: () -> Unit) {
    val colors = TodoTheme.colors
    val account = state.account
    Row(verticalAlignment = Alignment.CenterVertically) {
        UserAvatar(
            initials = account?.initials ?: "?",
            avatarUri = state.avatarUri,
            size = 48.dp,
            fontSize = 17.sp,
            modifier = Modifier.clickable(onClick = onOpenProfile)
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = formatHeaderDate(state.today), color = colors.textSecondary, fontSize = 14.sp)
            Text(
                text = "Chào ${account?.name ?: "khách"}!",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(colors.card)
                .clickable { actions.selectTab(TodoTab.NOTIFICATIONS) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = "Thông báo",
                tint = colors.textPrimary,
                modifier = Modifier.size(22.dp)
            )
            if (state.hasReminderAlert) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 11.dp)
                        .size(12.dp)
                        .background(colors.notificationDot, CircleShape)
                        .border(2.dp, colors.card, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun ProgressCard(doneCount: Int, totalCount: Int, percent: Int) {
    val colors = TodoTheme.colors
    val subtitle = when {
        totalCount == 0 -> "Hôm nay bạn chưa có việc nào."
        doneCount == totalCount -> "Tuyệt vời, bạn đã xong hết việc hôm nay!"
        else -> "Bắt đầu từ việc nhỏ nhất nhé."
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.accent, RoundedCornerShape(22.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "Tiến độ hôm nay", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
            Text(text = "$doneCount/$totalCount việc", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
        }
        ProgressRing(percent = percent)
    }
}

@Composable
private fun ProgressRing(percent: Int) {
    Box(modifier = Modifier.size(72.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 8.dp.toPx()
            val arcTopLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            drawArc(
                color = Color.White.copy(alpha = 0.28f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = Color.White,
                startAngle = -90f,
                sweepAngle = 360f * percent / 100f,
                useCenter = false,
                topLeft = arcTopLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth)
            )
        }
        Text(text = "$percent%", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CategoryCard(
    category: TaskCategory,
    iconRes: Int,
    remainingCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = TodoTheme.colors
    val cardShape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .clip(cardShape)
            .background(colors.card)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(colors.categoryChip(category), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = colors.categoryText(category),
                modifier = Modifier.size(20.dp)
            )
        }
        Text(text = category.label, color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        Text(text = "$remainingCount việc còn lại", color = colors.textSecondary, fontSize = 13.sp)
    }
}
