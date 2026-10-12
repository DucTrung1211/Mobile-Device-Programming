package com.example.tuan02project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan02project.ui.theme.Tuan02_ProjectTheme
import java.util.Locale

data class StudentScore(val name: String, val score: Double?)

@Composable
fun Bai3ForIfNullableScreen() {
    val students = listOf(
        StudentScore("Nguyễn Văn An", 9.0),
        StudentScore("Trần Thị Bình", 7.2),
        StudentScore("Lê Minh Chi", null),
        StudentScore("Phạm Quốc Dũng", 4.5),
        StudentScore("Võ Thanh Em", 5.8)
    )
    var totalScore = 0.0
    var scoredStudentCount = 0
    var excellentCount = 0
    var missingScoreCount = 0
    var highestScore = 0.0

    // Chỉ cộng và đếm những sinh viên đã có điểm.
    for (student in students) {
        if (student.score == null) {
            missingScoreCount++
        } else {
            val score = student.score
            totalScore += score
            scoredStudentCount++
            if (score >= 8.0) excellentCount++
            if (score > highestScore) highestScore = score
        }
    }
    val averageScore = if (scoredStudentCount > 0) totalScore / scoredStudentCount else 0.0

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = Color(0xFFF4F4F8),
        topBar = { ExerciseHeader("Bảng điểm lớp") }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(color = Color(0xFF304776), shape = RoundedCornerShape(14.dp)) {
                Column(Modifier.fillMaxWidth().padding(18.dp)) {
                    Text("Điểm trung bình lớp", color = Color.White, fontSize = 13.sp, lineHeight = 18.sp)
                    Text(String.format(Locale.US, "%.2f", averageScore),
                        color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("Giỏi", color = Color.White, fontSize = 12.sp, lineHeight = 16.sp)
                            Text(excellentCount.toString(), color = Color.White)
                        }
                        Column {
                            Text("Chưa có điểm", color = Color.White, fontSize = 12.sp, lineHeight = 16.sp)
                            Text(missingScoreCount.toString(), color = Color.White)
                        }
                        Column {
                            Text("Cao nhất", color = Color.White, fontSize = 12.sp, lineHeight = 16.sp)
                            Text(highestScore.toString(), color = Color.White)
                        }
                    }
                }
            }
            Text("DANH SÁCH · ${students.size} SINH VIÊN", color = Color.Gray, fontSize = 12.sp, lineHeight = 16.sp)
            for (student in students) {
                val score = student.score
                val result = if (score == null) "Chưa có điểm" else classifyScore(score)
                Surface(color = Color.White, shape = RoundedCornerShape(10.dp)) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(student.name, color = Color(0xFF20242C), fontWeight = FontWeight.Medium)
                            Text(result, color = classificationColor(result), fontSize = 13.sp, lineHeight = 18.sp)
                        }
                        Text(score?.toString() ?: "—", fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (score == null) Color.Gray else Color(0xFF20242C))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Bai3Preview() {
    Tuan02_ProjectTheme { Bai3ForIfNullableScreen() }
}
