package com.example.tuan04project.features.todo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.features.todo.model.OnboardingIllustration
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TodoBlue
import com.example.tuan04project.ui.theme.TodoBlueLight
import com.example.tuan04project.ui.theme.TodoLifeChip
import com.example.tuan04project.ui.theme.TodoLifeText
import com.example.tuan04project.ui.theme.TodoLineGray
import com.example.tuan04project.ui.theme.TodoStudyChip

@Composable
fun OnboardingIllustrationCard(illustration: OnboardingIllustration, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(0.85f)
            .background(TodoBlueLight, RoundedCornerShape(24.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        when (illustration) {
            OnboardingIllustration.TASK_LIST -> TaskListIllustration()
            OnboardingIllustration.TASK_GROUPS -> TaskGroupsIllustration()
            OnboardingIllustration.REMINDER -> ReminderIllustration()
        }
    }
}

@Composable
private fun TaskListIllustration() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TaskRow(isDone = true)
        TaskRow(isDone = false)
        TaskRow(isDone = false)
    }
}

@Composable
private fun TaskRow(isDone: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .shadow(1.dp, RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isDone) {
            Box(
                modifier = Modifier.size(16.dp).background(TodoBlue, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        } else {
            Box(Modifier.size(16.dp).border(1.5.dp, TodoLineGray, CircleShape))
        }
        Spacer(Modifier.width(10.dp))
        GrayLine(Modifier.fillMaxWidth(0.85f))
    }
}

@Composable
private fun TaskGroupsIllustration() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TaskGroupCard(groupName = "Học tập", chipColor = TodoStudyChip, textColor = TodoBlue)
        TaskGroupCard(groupName = "Cuộc sống", chipColor = TodoLifeChip, textColor = TodoLifeText)
    }
}

@Composable
private fun TaskGroupCard(groupName: String, chipColor: Color, textColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Text(
            text = groupName,
            modifier = Modifier
                .background(chipColor, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp),
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(10.dp))
        GrayLine(Modifier.fillMaxWidth(0.8f))
    }
}

@Composable
private fun ReminderIllustration() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .shadow(2.dp, CircleShape)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = null,
                tint = TodoBlue,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.height(22.dp))
        Text(
            text = "Ôn bài Toán lúc 19:00",
            modifier = Modifier
                .shadow(1.dp, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            color = TextDark,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun GrayLine(modifier: Modifier = Modifier) {
    Box(modifier.height(5.dp).background(TodoLineGray, RoundedCornerShape(50)))
}
