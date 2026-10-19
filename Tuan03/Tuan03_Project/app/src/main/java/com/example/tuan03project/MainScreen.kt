package com.example.tuan03project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan03project.ui.theme.Tuan03ProjectTheme

@Composable
fun MainScreen() {

    var daNhanNut by remember { mutableStateOf(false) }
    val mauChu = Color(0xFF202020)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().weight(0.42f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "My First App",
                color = mauChu,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(0.10f),
            contentAlignment = Alignment.Center
        ) {
            if (daNhanNut) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "I'm",
                        color = mauChu,
                        fontSize = 18.sp,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "Nguyen Van A",
                        color = mauChu,
                        fontSize = 18.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = "Hello",
                    color = mauChu,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(0.48f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {

                    daNhanNut = true
                },
                modifier = Modifier.width(180.dp).height(54.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0068BE),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Say Hi!", fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MainScreenPreview() {
    Tuan03ProjectTheme {
        MainScreen()
    }
}
