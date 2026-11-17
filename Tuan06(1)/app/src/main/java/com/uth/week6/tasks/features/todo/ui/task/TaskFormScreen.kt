package com.uth.week6.tasks.features.todo.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.week6.tasks.R
import com.uth.week6.tasks.features.todo.model.TaskCategory
import com.uth.week6.tasks.features.todo.model.TaskDraft
import com.uth.week6.tasks.features.todo.model.TaskPriority
import com.uth.week6.tasks.features.todo.ui.common.TodoSwitch
import com.uth.week6.tasks.features.todo.ui.common.formatShortDate
import com.uth.week6.tasks.features.todo.ui.common.formatTime
import com.uth.week6.tasks.features.todo.ui.common.toUtcMillis
import com.uth.week6.tasks.features.todo.ui.common.utcMillisToLocalDate
import com.uth.week6.tasks.features.todo.ui.theme.TodoTheme
import com.uth.week6.tasks.features.todo.ui.theme.categoryChip
import com.uth.week6.tasks.features.todo.ui.theme.categoryText
import com.uth.week6.tasks.features.todo.viewmodel.TodoUiState
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun TaskFormScreen(state: TodoUiState, taskId: String?, onClose: () -> Unit, onSave: (String?, TaskDraft) -> Unit, onDelete: (String) -> Unit) {
    val colors = TodoTheme.colors
    val existingTask = remember(taskId) { taskId?.let { state.findTask(it) } }

    var title by rememberSaveable { mutableStateOf(existingTask?.title ?: "") }
    var note by rememberSaveable { mutableStateOf(existingTask?.note ?: "") }
    var category by rememberSaveable { mutableStateOf(existingTask?.category ?: TaskCategory.STUDY) }
    var dateValue by rememberSaveable { mutableStateOf((existingTask?.date ?: state.today).toEpochDay()) }
    val date = LocalDate.ofEpochDay(dateValue)
    var timeValue by rememberSaveable { mutableStateOf((existingTask?.time ?: LocalTime.of(19, 0)).toSecondOfDay()) }
    val time = LocalTime.ofSecondOfDay(timeValue.toLong())
    var isReminderOn by rememberSaveable { mutableStateOf(existingTask?.isReminderOn ?: true) }
    var priority by rememberSaveable { mutableStateOf(existingTask?.priority ?: TaskPriority.MEDIUM) }
    var deleteConfirmation by remember { mutableStateOf(false) }
    var isTitleErrorVisible by remember { mutableStateOf(false) }
    var isDatePickerVisible by remember { mutableStateOf(false) }
    var isTimePickerVisible by remember { mutableStateOf(false) }

    val saveTask = {
        val draft = TaskDraft(title, note, category, date, time, isReminderOn, priority)
        if (title.isBlank()) isTitleErrorVisible = true
        else if (!state.busy) onSave(taskId, draft)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)

            .imePadding()
    ) {
        FormTopBar(
            title = if (taskId == null) "Việc mới" else "Sửa công việc",
            onCancel = onClose,
            onSave = { if (!state.busy) saveTask() }
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Column {
                FormCard {
                    FormTextField(
                        label = "Tên công việc",
                        value = title,
                        placeholder = "Nhập tên công việc",
                        onValueChange = {
                            title = it
                            isTitleErrorVisible = false
                        },
                        fontSize = 17,
                        isSingleLine = true
                    )
                    HorizontalDivider(color = colors.divider)
                    FormTextField(
                        label = "Ghi chú",
                        value = note,
                        placeholder = "Thêm ghi chú (không bắt buộc)",
                        onValueChange = { note = it },
                        fontSize = 15,
                        isSingleLine = false
                    )
                }
                if (isTitleErrorVisible) {
                    Text(
                        text = "Vui lòng nhập tên công việc",
                        modifier = Modifier.padding(top = 6.dp, start = 4.dp),
                        color = colors.danger,
                        fontSize = 13.sp
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FormLabel(text = "Nhóm")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    TaskCategory.entries.forEach { option ->
                        CategoryButton(
                            category = option,
                            isSelected = category == option,
                            onClick = { category = option },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            FormCard {
                FormValueRow(
                    iconRes = R.drawable.ic_todo_calendar,
                    label = "Ngày",
                    value = formatShortDate(date),
                    onClick = { isDatePickerVisible = true }
                )
                HorizontalDivider(color = colors.divider)
                FormValueRow(
                    iconRes = R.drawable.ic_todo_clock,
                    label = "Giờ",
                    value = formatTime(time),
                    onClick = { isTimePickerVisible = true }
                )
                HorizontalDivider(color = colors.divider)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormIcon(iconRes = R.drawable.ic_bell)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Nhắc tôi", color = colors.textPrimary, fontSize = 15.sp)
                        Text(text = "Chưa có lịch nhắc tự động", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    TodoSwitch(checked = isReminderOn, onCheckedChange = { isReminderOn = it })
                }
            }

            if (taskId != null) {
                TextButton(enabled = !state.busy, onClick = { deleteConfirmation = true }) { Text("Xóa công việc", color = colors.danger) }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FormLabel(text = "Mức ưu tiên")
                PrioritySelector(selectedPriority = priority, onPrioritySelected = { priority = it })
            }
        }
        Button(
            enabled = !state.busy,
            onClick = saveTask,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent)
        ) {
            Text(text = if (state.busy) "Đang lưu…" else "Lưu công việc", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        }
    }

    if (deleteConfirmation && taskId != null) {
        com.uth.week6.tasks.features.todo.ui.common.TodoConfirmDialog(
            "Xóa công việc?", "Công việc này sẽ bị xóa khỏi tài khoản Firebase.", "Xóa",
            { deleteConfirmation = false; onDelete(taskId) }, { deleteConfirmation = false })
    }
    if (isDatePickerVisible) {
        TaskDatePickerDialog(
            initialDate = date,
            onDateSelected = { selectedDate ->
                dateValue = selectedDate.toEpochDay()
                isDatePickerVisible = false
            },
            onDismiss = { isDatePickerVisible = false }
        )
    }
    if (isTimePickerVisible) {
        TaskTimePickerDialog(
            initialTime = time,
            onTimeSelected = { selectedTime ->
                timeValue = selectedTime.toSecondOfDay()
                isTimePickerVisible = false
            },
            onDismiss = { isTimePickerVisible = false }
        )
    }
}

@Composable
private fun FormTopBar(title: String, onCancel: () -> Unit, onSave: () -> Unit) {
    val colors = TodoTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            text = "Hủy",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onCancel)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            color = colors.accentText,
            fontSize = 16.sp
        )
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = colors.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Lưu",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onSave)
                .padding(horizontal = 8.dp, vertical = 10.dp),
            color = colors.accentText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FormCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(TodoTheme.colors.card, RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(text = text, color = TodoTheme.colors.textSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun FormIcon(iconRes: Int) {
    Icon(
        painter = painterResource(iconRes),
        contentDescription = null,
        tint = TodoTheme.colors.textSecondary,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
private fun FormTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    fontSize: Int,
    isSingleLine: Boolean
) {
    val colors = TodoTheme.colors
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label, color = colors.textSecondary, fontSize = 12.sp)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = isSingleLine,
            minLines = if (isSingleLine) 1 else 2,
            textStyle = TextStyle(fontFamily = com.uth.week6.tasks.features.todo.ui.theme.TodoFontFamily,
                color = colors.textPrimary,
                fontSize = fontSize.sp,
                fontWeight = if (isSingleLine) FontWeight.Medium else FontWeight.Normal
            ),
            cursorBrush = SolidColor(colors.accent),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = colors.textSecondary, fontSize = fontSize.sp)
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun CategoryButton(
    category: TaskCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = TodoTheme.colors
    val buttonShape = RoundedCornerShape(14.dp)
    val buttonModifier = if (isSelected) {
        Modifier
            .background(colors.categoryChip(category), buttonShape)
            .border(2.dp, colors.categoryText(category), buttonShape)
    } else {
        Modifier
            .background(colors.card, buttonShape)
            .border(1.dp, colors.outline, buttonShape)
    }
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(buttonShape)
            .then(buttonModifier)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = category.label,
            color = if (isSelected) colors.categoryText(category) else colors.textBody,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun FormValueRow(iconRes: Int, label: String, value: String, onClick: () -> Unit) {
    val colors = TodoTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(53.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FormIcon(iconRes = iconRes)
        Text(text = label, modifier = Modifier.weight(1f), color = colors.textPrimary, fontSize = 15.sp)
        Text(
            text = value,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(colors.valueButton)
                .clickable(onClick = onClick)
                .padding(horizontal = 10.dp, vertical = 6.dp),
            color = colors.textPrimary,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun PrioritySelector(selectedPriority: TaskPriority, onPrioritySelected: (TaskPriority) -> Unit) {
    val colors = TodoTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colors.fieldBackground, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        TaskPriority.entries.forEach { priority ->
            val isSelected = priority == selectedPriority
            val segmentShape = RoundedCornerShape(9.dp)
            val segmentModifier = if (isSelected) {
                Modifier
                    .shadow(1.dp, segmentShape)
                    .background(colors.segmentSelected, segmentShape)
            } else {
                Modifier
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .then(segmentModifier)
                    .clip(segmentShape)
                    .clickable { onPrioritySelected(priority) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = priority.label,
                    color = if (isSelected) colors.textPrimary else colors.textLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate.toUtcMillis())
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedMillis = datePickerState.selectedDateMillis
                if (selectedMillis != null) {
                    onDateSelected(utcMillisToLocalDate(selectedMillis))
                } else {
                    onDismiss()
                }
            }) {
                Text("Chọn")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(text = "Chọn ngày", modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp))
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskTimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = TodoTheme.colors.card,
        title = { Text(text = "Chọn giờ", color = TodoTheme.colors.textPrimary) },
        text = { TimePicker(state = timePickerState) },
        confirmButton = {
            TextButton(onClick = { onTimeSelected(LocalTime.of(timePickerState.hour, timePickerState.minute)) }) {
                Text("Chọn")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
