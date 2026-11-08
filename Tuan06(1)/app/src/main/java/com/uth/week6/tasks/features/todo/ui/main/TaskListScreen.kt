package com.uth.week6.tasks.features.todo.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.week6.tasks.R
import com.uth.week6.tasks.features.todo.model.TaskCategory
import com.uth.week6.tasks.features.todo.ui.common.TaskRow
import com.uth.week6.tasks.features.todo.ui.common.TodoSectionLabel
import com.uth.week6.tasks.features.todo.ui.common.formatDueLabel
import com.uth.week6.tasks.features.todo.ui.common.rememberTaskSound
import com.uth.week6.tasks.features.todo.ui.theme.TodoTheme
import com.uth.week6.tasks.features.todo.viewmodel.TodoUiState
import com.uth.week6.tasks.features.todo.viewmodel.TodoActions

@Composable
fun TaskListScreen(state: TodoUiState, actions: TodoActions, onOpenTask: (String) -> Unit) {
    val colors = TodoTheme.colors
    val playSound = rememberTaskSound(state.settings.isSoundOn)
    val pendingTasks = state.filteredPendingTasks
    val doneTasks = state.filteredDoneTasks

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            ,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Danh sách",
                    modifier = Modifier.weight(1f),
                    color = colors.textPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.4).sp
                )
                Text(text = "${state.pendingCount} việc chưa xong", color = colors.textSecondary, fontSize = 14.sp)
            }
            Spacer(Modifier.height(16.dp))
            SearchField(query = state.searchQuery, onQueryChange = { actions.updateSearchQuery(it) })
            Spacer(Modifier.height(16.dp))
            CategoryFilterRow(
                selectedCategory = state.categoryFilter,
                onCategorySelected = { actions.updateCategoryFilter(it) }
            )
            Spacer(Modifier.height(20.dp))
            TodoSectionLabel(text = "Cần làm")
        }
        if (pendingTasks.isEmpty()) {
            item { EmptyListText(text = "Không có công việc nào cần làm.") }
        }
        items(pendingTasks, key = { task -> task.id }) { task ->
            TaskRow(
                task = task,
                timeLabel = formatDueLabel(task.date, task.time, state.today),
                showUrgentBadge = true,
                showChevron = false,
                onToggleDone = {
                    playSound()
                    actions.toggleTaskDone(task.id)
                },
                onClick = { onOpenTask(task.id) }
            )
        }
        item {
            Spacer(Modifier.height(10.dp))
            TodoSectionLabel(text = "Đã xong")
        }
        if (doneTasks.isEmpty()) {
            item { EmptyListText(text = "Chưa có công việc nào hoàn thành.") }
        }
        items(doneTasks, key = { task -> task.id }) { task ->
            TaskRow(
                task = task,
                timeLabel = formatDueLabel(task.date, task.time, state.today),
                showUrgentBadge = false,
                showChevron = false,
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
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    val colors = TodoTheme.colors
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = TextStyle(fontFamily = com.uth.week6.tasks.features.todo.ui.theme.TodoFontFamily, color = colors.textPrimary, fontSize = 16.sp),
        cursorBrush = SolidColor(colors.accent),
        modifier = Modifier.fillMaxWidth(),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(colors.fieldBackground, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_todo_search),
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(text = "Tìm công việc", color = colors.textSecondary, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun CategoryFilterRow(selectedCategory: TaskCategory?, onCategorySelected: (TaskCategory?) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterTab(label = "Tất cả", isSelected = selectedCategory == null, onClick = { onCategorySelected(null) })
        TaskCategory.entries.forEach { category ->
            FilterTab(
                label = category.label,
                isSelected = selectedCategory == category,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
private fun FilterTab(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val colors = TodoTheme.colors
    val tabShape = RoundedCornerShape(18.dp)
    val tabModifier = if (isSelected) {
        Modifier.background(colors.textPrimary, tabShape)
    } else {
        Modifier
            .background(colors.card, tabShape)
            .border(1.dp, colors.outline, tabShape)
    }
    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(tabShape)
            .then(tabModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) colors.background else colors.textBody,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun EmptyListText(text: String) {
    Text(text = text, color = TodoTheme.colors.textSecondary, fontSize = 14.sp)
}
