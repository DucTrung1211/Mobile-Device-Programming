package com.example.tuan04project.features.todo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.features.todo.model.TodoAppInfo
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray
import com.example.tuan04project.ui.theme.TodoBlue
import com.example.tuan04project.ui.theme.TodoLineGray

@Composable
fun TodoAppIcon(iconSize: Dp, backgroundColor: Color, contentColor: Color) {
    Box(
        modifier = Modifier
            .size(iconSize)
            .background(backgroundColor, RoundedCornerShape(iconSize * 0.24f)),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(iconSize * 0.08f)) {
            DoneTaskMark(iconSize, contentColor, backgroundColor)
            PendingTaskMark(iconSize, contentColor)
        }
    }
}

@Composable
private fun DoneTaskMark(iconSize: Dp, markColor: Color, checkColor: Color) {
    val circleSize = iconSize * 0.2f
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(circleSize).background(markColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.size(circleSize * 0.75f)
            )
        }
        Spacer(Modifier.width(iconSize * 0.07f))
        Box(
            Modifier
                .size(width = iconSize * 0.36f, height = iconSize * 0.09f)
                .background(markColor, RoundedCornerShape(50))
        )
    }
}

@Composable
private fun PendingTaskMark(iconSize: Dp, markColor: Color) {
    val circleSize = iconSize * 0.2f
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier
                .size(circleSize)
                .border(iconSize * 0.035f, markColor.copy(alpha = 0.7f), CircleShape)
        )
        Spacer(Modifier.width(iconSize * 0.07f))
        Box(
            Modifier
                .size(width = iconSize * 0.28f, height = iconSize * 0.09f)
                .background(markColor.copy(alpha = 0.7f), RoundedCornerShape(50))
        )
    }
}

@Composable
fun TodoHeader(appInfo: TodoAppInfo, onSkip: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TodoAppIcon(iconSize = 24.dp, backgroundColor = TodoBlue, contentColor = Color.White)
        Spacer(Modifier.width(8.dp))
        Text(
            text = appInfo.brandName,
            color = TextDark,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = " ${appInfo.productName}",
            color = TodoBlue,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.weight(1f))
        TextButton(onClick = onSkip) {
            Text(text = "Bỏ qua", color = TextGray, fontSize = 13.sp)
        }
    }
}

@Composable
fun PageIndicator(pageCount: Int, currentPageIndex: Int) {
    Row(
        modifier = Modifier.semantics {
            contentDescription = "Trang ${currentPageIndex + 1} trên $pageCount"
        },
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isCurrentPage = index == currentPageIndex
            Box(
                Modifier
                    .size(width = if (isCurrentPage) 18.dp else 5.dp, height = 5.dp)
                    .background(
                        color = if (isCurrentPage) TodoBlue else TodoLineGray,
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}
