package com.example.tuan05project.features.todo.ui.common

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.SoundEffectConstants
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan05project.R
import com.example.tuan05project.features.todo.model.Task
import com.example.tuan05project.features.todo.model.TaskCategory
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import com.example.tuan05project.features.todo.ui.theme.categoryChip
import com.example.tuan05project.features.todo.ui.theme.categoryText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TaskCheckCircle(isDone: Boolean, onClick: () -> Unit) {
    val colors = TodoTheme.colors
    val circleModifier = Modifier
        .size(26.dp)
        .clip(CircleShape)
        .clickable(onClick = onClick)
        .semantics { contentDescription = if (isDone) "Bỏ đánh dấu" else "Đánh dấu xong" }
    if (isDone) {
        Box(
            modifier = circleModifier.background(colors.accent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    } else {
        Box(modifier = circleModifier.border(2.dp, colors.circleBorder, CircleShape))
    }
}

@Composable
fun CategoryChip(category: TaskCategory, isLarge: Boolean = false) {
    val colors = TodoTheme.colors
    Text(
        text = category.label,
        modifier = Modifier
            .background(colors.categoryChip(category), RoundedCornerShape(if (isLarge) 8.dp else 6.dp))
            .padding(horizontal = if (isLarge) 10.dp else 8.dp, vertical = if (isLarge) 4.dp else 2.dp),
        color = colors.categoryText(category),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun TaskRow(
    task: Task,
    timeLabel: String?,
    showUrgentBadge: Boolean,
    showChevron: Boolean,
    onToggleDone: () -> Unit,
    onClick: () -> Unit
) {
    val colors = TodoTheme.colors
    val rowShape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(rowShape)
            .background(colors.card)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TaskCheckCircle(isDone = task.isDone, onClick = onToggleDone)
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = task.title,
                color = if (task.isDone) colors.textDone else colors.textPrimary,
                fontSize = 15.sp,
                fontWeight = if (timeLabel == null) FontWeight.Normal else FontWeight.Medium,
                textDecoration = if (task.isDone) TextDecoration.LineThrough else null,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (timeLabel != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryChip(category = task.category)
                    Text(text = timeLabel, color = colors.textSecondary, fontSize = 12.sp)
                }
            }
        }
        if (showUrgentBadge && task.isUrgent) {
            Text(
                text = "Gấp",
                modifier = Modifier
                    .background(colors.urgentChip, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                color = colors.urgentText,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (showChevron) {
            Icon(
                painter = painterResource(R.drawable.ic_todo_chevron_right),
                contentDescription = null,
                tint = colors.chevron,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun TodoSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        modifier = modifier,
        color = TodoTheme.colors.textSecondary,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp
    )
}

@Composable
fun TodoSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val colors = TodoTheme.colors
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = colors.switchOn,
            checkedBorderColor = Color.Transparent,
            uncheckedThumbColor = Color.White,
            uncheckedTrackColor = colors.switchOff,
            uncheckedBorderColor = Color.Transparent
        )
    )
}

@Composable
fun TodoTopBar(
    title: String,
    backLabel: String?,
    actionLabel: String?,
    onBack: () -> Unit,
    onAction: () -> Unit = {}
) {
    val colors = TodoTheme.colors
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onBack)
                .padding(horizontal = 6.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_todo_back),
                contentDescription = "Quay lại",
                tint = colors.accentText,
                modifier = Modifier.size(22.dp)
            )
            if (backLabel != null) {
                Text(text = backLabel, color = colors.accentText, fontSize = 16.sp)
            }
        }
        Text(
            text = title,
            modifier = Modifier.align(Alignment.Center),
            color = colors.textPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onAction)
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                color = colors.accentText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun UserAvatar(
    initials: String,
    avatarUri: Any?,
    size: Dp,
    fontSize: TextUnit,
    modifier: Modifier = Modifier
) {
    val colors = TodoTheme.colors

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(colors.accentSoft),
        contentAlignment = Alignment.Center
    ) {
        Text(text = initials, color = colors.onAccentSoft, fontSize = fontSize, fontWeight = FontWeight.Bold)
        if (avatarUri != null) {
            coil.compose.AsyncImage(model = avatarUri, contentDescription = "Ảnh đại diện",
                contentScale = ContentScale.Crop, modifier = Modifier.size(size))
        }
    }
}

@Composable
fun rememberTaskSound(isEnabled: Boolean): () -> Unit {
    val view = LocalView.current
    return {
        if (isEnabled) {
            view.playSoundEffect(SoundEffectConstants.CLICK)
        }
    }
}

@Composable
fun TodoSnackbarHost(message: String?, onMessageShown: () -> Unit, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            onMessageShown()
        }
    }
    SnackbarHost(hostState = snackbarHostState, modifier = modifier) { data ->
        Snackbar(snackbarData = data, shape = RoundedCornerShape(12.dp))
    }
}

@Composable
fun TodoInfoDialog(title: String, text: String, onDismiss: () -> Unit) {
    val colors = TodoTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.card,
        title = { Text(text = title, color = colors.textPrimary, fontWeight = FontWeight.Bold) },
        text = { Text(text = text, color = colors.textBody, fontSize = 15.sp, lineHeight = 22.sp) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Đã hiểu", color = colors.accentText)
            }
        }
    )
}

@Composable
fun TodoConfirmDialog(
    title: String,
    text: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = TodoTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.card,
        title = { Text(text = title, color = colors.textPrimary, fontWeight = FontWeight.Bold) },
        text = { Text(text = text, color = colors.textBody, fontSize = 15.sp) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, color = colors.danger, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Hủy", color = colors.textSecondary)
            }
        }
    )
}

@Composable
fun TodoTextInputDialog(
    title: String,
    initialValue: String,
    label: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = TodoTheme.colors
    var value by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.card,
        title = { Text(text = title, color = colors.textPrimary, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                label = { Text(label) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    focusedLabelColor = colors.accent,
                    unfocusedBorderColor = colors.outline,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary
                )
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(value) }, enabled = value.isNotBlank()) {
                Text(text = "Lưu", color = if (value.isNotBlank()) colors.accentText else colors.textDone)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Hủy", color = colors.textSecondary)
            }
        }
    )
}
