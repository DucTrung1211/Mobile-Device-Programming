package com.example.tuan02project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan02project.ui.theme.Tuan02_ProjectTheme

data class Student(val name: String, val email: String?, val phone: String?)

@Composable
fun Bai1NullableScreen() {
    val students = listOf(
        Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
        Student("Trần Thị Bình", null, "0912 888 999"),
        Student("Lê Minh Chi", "chi@sv.edu.vn", null),
        Student("Phạm Quốc Dũng", null, null),
        Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
    )
    var missingEmailCount = 0
    for (student in students) {
        if (student.email == null) missingEmailCount++
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = Color(0xFFF4F4F8),
        topBar = { ExerciseHeader("Danh sách sinh viên") }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (student in students) {
                Surface(color = Color.White, shape = RoundedCornerShape(10.dp)) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(student.name, fontWeight = FontWeight.Medium, color = Color(0xFF20242C))
                        Text("Email: ${student.email ?: "Chưa cập nhật"}",
                            color = if (student.email == null) Color(0xFFA55543) else Color.Gray,
                            fontSize = 14.sp, lineHeight = 20.sp)
                        Text("SĐT: ${student.phone ?: "Chưa cập nhật"}",
                            color = if (student.phone == null) Color(0xFFA55543) else Color.Gray,
                            fontSize = 14.sp, lineHeight = 20.sp)
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth().background(Color(0xFFE4EAF3),
                    RoundedCornerShape(8.dp)).padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tổng: ${students.size} sinh viên", color = Color(0xFF304776),
                    fontSize = 14.sp)
                Text("Thiếu email: $missingEmailCount", color = Color(0xFFA55543),
                    fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Bai1Preview() {
    Tuan02_ProjectTheme { Bai1NullableScreen() }
}
