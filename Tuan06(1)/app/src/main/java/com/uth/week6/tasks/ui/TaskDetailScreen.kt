package com.uth.week6.tasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uth.week6.tasks.R
import com.uth.week6.tasks.features.todo.ui.common.*
import com.uth.week6.tasks.features.todo.ui.theme.*
import com.uth.week6.tasks.features.todo.viewmodel.*

@Composable
fun TaskDetailScreen(state: TodoUiState, vm: TodoViewModel, onBack: () -> Unit) {
    val colors = TodoTheme.colors
    val task = state.detail
    var confirmDelete by rememberSaveable { mutableStateOf(false) }
    var editingIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var editingTitle by rememberSaveable { mutableStateOf("") }
    var newTitle by rememberSaveable(state.selectedId) { mutableStateOf("") }
    Column(Modifier.fillMaxSize().imePadding()) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack, enabled = !state.busy) { Text("‹ " + state.selectedTab.label) }
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { vm.edit(task?.id) }, enabled = task != null && !state.busy) { Text("Sửa") }
        }
        when {
            state.detailLoading -> StatusPanel("Đang tải chi tiết…")
            state.detailError != null -> StatusPanel(state.detailError) { state.selectedId?.let(vm::openTask) }
            task == null -> StatusPanel("Không tìm thấy công việc.")
            else -> {
                Column(
                    Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CategoryChip(task.category)
                        Text("Ưu tiên " + task.priority.label.lowercase(),
                            Modifier.background(colors.priorityChip(task.priority), RoundedCornerShape(6.dp)).padding(horizontal = 10.dp, vertical = 3.dp),
                            color = colors.priorityText(task.priority), fontSize = 12.sp)
                    }
                    Text(task.title, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
                    CardBlock {
                        DetailRow(R.drawable.ic_todo_calendar, "Hạn chót", formatDueLabel(task.date, task.time, state.today))
                        HorizontalDivider(color = colors.divider)
                        DetailRow(R.drawable.ic_bell, "Nhắc nhở", if (task.isReminderOn) "Đã bật tùy chọn" else "Tắt")
                    }
                    Text("Ghi chú", color = colors.textSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    CardBlock { Text(task.note.ifBlank { "Chưa có ghi chú." }, Modifier.padding(16.dp), color = colors.textBody, fontSize = 15.sp, lineHeight = 22.sp) }
                    Row {
                        Text("Việc nhỏ", Modifier.weight(1f), color = colors.textSecondary, fontSize = 13.sp)
                        Text("${task.completedSubtaskCount}/${task.subtasks.size}", fontSize = 13.sp, color = colors.textSecondary)
                    }
                    LinearProgressIndicator(
                        progress = { if (task.subtasks.isEmpty()) 0f else task.completedSubtaskCount.toFloat() / task.subtasks.size },
                        modifier = Modifier.fillMaxWidth().height(6.dp),
                        color = colors.accent, trackColor = colors.track
                    )
                    CardBlock {
                        task.subtasks.forEachIndexed { index, subtask ->
                            Row(Modifier.fillMaxWidth().heightIn(min = 53.dp).padding(start = 8.dp, end = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(subtask.isDone, onCheckedChange = { vm.changeSubtask(task.id, index) }, enabled = !state.busy)
                                Text(subtask.title, Modifier.weight(1f), fontSize = 15.sp,
                                    color = if (subtask.isDone) colors.textDone else colors.textPrimary,
                                    textDecoration = if (subtask.isDone) TextDecoration.LineThrough else null)
                                IconButton(onClick = { editingIndex = index; editingTitle = subtask.title }, enabled = !state.busy) {
                                    Icon(painterResource(R.drawable.ic_todo_edit), "Sửa việc nhỏ", Modifier.size(18.dp), tint = colors.textSecondary)
                                }
                                IconButton(onClick = { vm.changeSubtask(task.id, index, delete = true) }, enabled = !state.busy) {
                                    Icon(painterResource(R.drawable.ic_todo_trash), "Xóa việc nhỏ", Modifier.size(18.dp), tint = colors.danger)
                                }
                            }
                            HorizontalDivider(color = colors.divider)
                        }
                        Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(newTitle, { newTitle = it }, Modifier.weight(1f), placeholder = { Text("Thêm việc nhỏ", fontSize = 14.sp) }, singleLine = true, enabled = !state.busy)
                            TextButton(onClick = { vm.changeSubtask(task.id, null, newTitle); newTitle = "" }, enabled = newTitle.isNotBlank() && !state.busy) { Text("Thêm") }
                        }
                    }
                    if (task.isReminderOn) Text("Chỉ lưu trường remind; chưa lập lịch thông báo nền.", fontSize = 12.sp, color = colors.textSecondary)
                    Spacer(Modifier.height(8.dp))
                }
                Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = { vm.toggleTaskDone(task.id) }, enabled = !state.busy,
                        modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) {
                        Text(if (task.isDone) "Đánh dấu chưa xong" else "Đánh dấu hoàn thành", fontWeight = FontWeight.SemiBold)
                    }
                    TextButton(onClick = { confirmDelete = true }, enabled = !state.busy) { Text("Xóa công việc", color = colors.danger) }
                }
            }
        }
    }
    if (confirmDelete && task != null) AlertDialog(
        onDismissRequest = { confirmDelete = false }, title = { Text("Xóa công việc?") },
        text = { Text("Xóa “${task.title}” khỏi máy chủ?") },
        confirmButton = { TextButton(onClick = { confirmDelete = false; vm.deleteTask(task.id) }) { Text("Xóa") } },
        dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Hủy") } }
    )
    if (editingIndex != null && task != null) AlertDialog(
        onDismissRequest = { editingIndex = null }, title = { Text("Sửa việc nhỏ") },
        text = { OutlinedTextField(editingTitle, { editingTitle = it }, singleLine = true) },
        confirmButton = { TextButton(enabled = editingTitle.isNotBlank(), onClick = { vm.changeSubtask(task.id, editingIndex, editingTitle); editingIndex = null }) { Text("Lưu") } },
        dismissButton = { TextButton(onClick = { editingIndex = null }) { Text("Hủy") } }
    )
}

@Composable
fun CardBlock(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().background(TodoTheme.colors.card, RoundedCornerShape(16.dp)), content = content)
}

@Composable
private fun DetailRow(icon: Int, label: String, value: String) {
    Row(Modifier.fillMaxWidth().heightIn(min = 51.dp).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(painterResource(icon), null, Modifier.size(20.dp), tint = TodoTheme.colors.textSecondary)
        Text(label, Modifier.weight(1f), color = TodoTheme.colors.textSecondary, fontSize = 15.sp)
        Text(value, color = TodoTheme.colors.textPrimary, fontSize = 14.sp)
    }
}
