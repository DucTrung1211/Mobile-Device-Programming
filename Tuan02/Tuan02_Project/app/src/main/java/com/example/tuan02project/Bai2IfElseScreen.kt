package com.example.tuan02project

import androidx.compose.foundation.background
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

fun classifyScore(score: Double): String {
    if (score < 0 || score > 10 || score.isNaN()) {
        return "Không hợp lệ"
    } else if (score >= 8.0) {
        return "Giỏi"
    } else if (score >= 6.5) {
        return "Khá"
    } else if (score >= 5.0) {
        return "Trung bình"
    } else {
        return "Yếu"
    }
}

fun classificationColor(result: String): Color {
    if (result == "Giỏi") {
        return Color(0xFF3D7448)
    } else if (result == "Khá") {
        return Color(0xFF355F90)
    } else if (result == "Trung bình") {
        return Color(0xFF936829)
    } else if (result == "Yếu") {
        return Color(0xFFAB4D45)
    } else {
        return Color(0xFF777B85)
    }
}

@Composable
fun Bai2IfElseScreen() {
    val score = 8.5
    val result = classifyScore(score)
    val testScores = listOf(9.0, 7.0, 5.5, 3.0, 11.0)

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = Color(0xFFF4F4F8),
        topBar = { ExerciseHeader("Xếp loại học lực") }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding)
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(color = Color.White, shape = RoundedCornerShape(12.dp)) {
                Column(
                    Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Điểm trung bình", color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
                    Text(score.toString(), fontSize = 48.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF20242C))
                    Box(
                        Modifier.fillMaxWidth().background(classificationColor(result).copy(alpha = 0.1f),
                            RoundedCornerShape(8.dp)).padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(result, color = classificationColor(result),
                            fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }
            Text("KIỂM TRA THÊM", color = Color.Gray, fontSize = 12.sp)
            Surface(color = Color.White, shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    for (testScore in testScores) {
                        val testResult = classifyScore(testScore)
                        Row(
                            Modifier.fillMaxWidth().padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Điểm ${testScore.toString().removeSuffix(".0")}",
                                color = Color(0xFF20242C))
                            Text(testResult, color = classificationColor(testResult),
                                fontSize = 12.sp, lineHeight = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.background(
                                    classificationColor(testResult).copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                ).padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Bai2Preview() {
    Tuan02_ProjectTheme { Bai2IfElseScreen() }
}
