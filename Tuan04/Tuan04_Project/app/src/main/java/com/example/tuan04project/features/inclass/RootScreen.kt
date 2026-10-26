package com.example.tuan04project.features.inclass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.ui.theme.XanhDuong

@Composable
fun RootScreen(onPush: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.compose_logo),
                contentDescription = "Jetpack Compose",
                modifier = Modifier.size(200.dp)
            )
            Spacer(Modifier.height(28.dp))
            Text(
                text = "Navigation",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Navigation is a framework that simplifies the implementation of navigation between different UI components (activities, fragments, or composables) in an app.",
                color = Color.DarkGray,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = onPush,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = XanhDuong)
        ) {
            Text("PUSH", color = Color.White)
        }
        Spacer(Modifier.height(40.dp))
    }
}
