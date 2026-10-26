package com.example.tuan04project.features.inclass

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.ui.theme.XanhDuong

@Composable
fun DetailScreen(soThuTu: Int, onBack: () -> Unit, onBackToRoot: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(tieuDe = "Detail", onBack = onBack)
        Text(
            text = "\"$NOI_DUNG\"",
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp),
            color = Color.Black,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f).fillMaxHeight()
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFFC6F2FA), Color(0xFF28A8EF))),
                        RoundedCornerShape(8.dp)
                    )
                    .semantics { contentDescription = "Chi tiết mục $soThuTu" }
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "\"$NOI_DUNG\"",
                    color = Color.Black,
                    fontSize = 24.sp,
                    lineHeight = 32.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(20.dp))
                Text("Steve Jobs", color = Color.Black, fontSize = 12.sp)
            }
        }
        Button(
            onClick = onBackToRoot,
            modifier = Modifier.padding(horizontal = 28.dp).fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = XanhDuong)
        ) {
            Text("BACK TO ROOT", color = Color.White)
        }
        Spacer(Modifier.height(40.dp))
    }
}
