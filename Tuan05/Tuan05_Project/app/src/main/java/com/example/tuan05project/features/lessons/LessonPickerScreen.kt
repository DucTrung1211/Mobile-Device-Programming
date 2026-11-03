package com.example.tuan05project.features.lessons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan05project.R
import com.example.tuan05project.features.todo.ui.theme.TodoTheme
import com.example.tuan05project.navigation.Lesson

private val descriptions = mapOf(
    Lesson.AUTH to "Đăng nhập Google qua Credential Manager, thông báo lỗi/thành công, trang chủ và hồ sơ người dùng thật.",
    Lesson.FIREBASE to "UTH Todo đồng bộ Realtime Database theo UID, ảnh hồ sơ Storage, FCM, Analytics, Remote Config, Crashlytics, AdMob thử nghiệm.",
    Lesson.LEGACY to "Bài đăng nhập Gmail đã làm trước đó, giữ nguyên giao diện cũ."
)

@Composable
fun LessonPickerScreen(signedInName: String?, onSelect: (Lesson) -> Unit) {
    val colors = TodoTheme.colors
    Column(
        Modifier.fillMaxSize().background(colors.background).safeDrawingPadding()
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Image(painterResource(R.drawable.ic_todo_logo_small), null,
                Modifier.size(52.dp).background(colors.accent, RoundedCornerShape(16.dp)))
            Column {
                Text("Tuần 05 · UTH Todo", color = colors.textPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Chọn bài thực hành Firebase", color = colors.textSecondary, fontSize = 14.sp)
            }
        }
        Text(
            if (signedInName != null) "Phiên Firebase hiện tại: $signedInName. Đăng xuất trong Hồ sơ của từng bài."
            else "Chưa đăng nhập Firebase.",
            color = colors.textSecondary, fontSize = 13.sp
        )
        Spacer(Modifier.height(4.dp))
        listOf(Lesson.AUTH, Lesson.FIREBASE, Lesson.LEGACY).forEach { lesson ->
            LessonCard(lesson, descriptions.getValue(lesson)) { onSelect(lesson) }
        }
    }
}

@Composable
private fun LessonCard(lesson: Lesson, description: String, onClick: () -> Unit) {
    val colors = TodoTheme.colors
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(colors.card)
            .clickable(onClickLabel = "Mở ${lesson.code}", onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(lesson.code, color = colors.accentText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(lesson.title, color = colors.textPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(description, color = colors.textSecondary, fontSize = 13.sp, lineHeight = 18.sp)
        }
        Icon(painterResource(R.drawable.ic_todo_chevron_right), null, tint = colors.chevron, modifier = Modifier.size(18.dp))
    }
}
