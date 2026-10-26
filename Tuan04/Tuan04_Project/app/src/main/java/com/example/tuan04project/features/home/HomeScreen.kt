package com.example.tuan04project.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.ui.theme.BorderGray
import com.example.tuan04project.ui.theme.LibraryBlue
import com.example.tuan04project.ui.theme.SmartBlue
import com.example.tuan04project.ui.theme.TextDark
import com.example.tuan04project.ui.theme.TextGray
import com.example.tuan04project.ui.theme.TodoBlue
import com.example.tuan04project.ui.theme.XanhDuong

@Composable
fun HomeScreen(
    onOpenTodo: () -> Unit,
    onOpenLibrary: () -> Unit,
    onOpenDataFlow: () -> Unit,
    onOpenInClass: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(text = "Bài tập Tuần 04", color = TextDark, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(text = "Lập trình Thiết bị Di động • Jetpack Compose", color = TextGray, fontSize = 14.sp)
        Spacer(Modifier.height(24.dp))

        ExerciseCard(
            badgeText = "1",
            badgeColor = TodoBlue,
            title = "Navigation + OOP",
            description = "UTH Todo V2: Splash và 3 trang onboarding",
            onClick = onOpenTodo
        )
        Spacer(Modifier.height(12.dp))
        ExerciseCard(
            badgeText = "2",
            badgeColor = LibraryBlue,
            title = "Hệ thống Quản lý Thư viện",
            description = "Sách, sinh viên và lịch sử mượn sách",
            onClick = onOpenLibrary
        )
        Spacer(Modifier.height(12.dp))
        ExerciseCard(
            badgeText = "3",
            badgeColor = SmartBlue,
            title = "Data Flow Navigation",
            description = "SmartTasks: truyền email, OTP, mật khẩu qua 4 màn hình",
            onClick = onOpenDataFlow
        )

        Spacer(Modifier.height(28.dp))
        Text(text = "Thực hành trên lớp", color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        ExerciseCard(
            badgeText = "TH",
            badgeColor = XanhDuong,
            title = "Navigation + LazyColumn",
            description = "Root, danh sách 1.000.000 mục và Detail",
            onClick = onOpenInClass
        )
    }
}

@Composable
private fun ExerciseCard(
    badgeText: String,
    badgeColor: Color,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .border(1.dp, BorderGray, cardShape)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(badgeColor, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = badgeText, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = TextDark, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(text = description, color = TextGray, fontSize = 13.sp)
        }
        Icon(
            painter = painterResource(R.drawable.ic_chevron),
            contentDescription = null,
            tint = TextGray,
            modifier = Modifier.size(22.dp)
        )
    }
}
