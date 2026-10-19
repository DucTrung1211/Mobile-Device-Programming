package com.example.tuan03project.bai01uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan03project.R

@Composable
fun IntroScreen(onReady: () -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier.weight(1f).fillMaxWidth().padding(bottom = 44.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.compose_logo),
                contentDescription = "Logo Jetpack Compose",
                modifier = Modifier.size(200.dp)
            )
            Spacer(Modifier.height(80.dp))
            Text("Jetpack Compose", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(24.dp))
            Text(
                "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                Modifier.fillMaxWidth(), fontSize = 14.sp, lineHeight = 19.sp,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = onReady,
            modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp).height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue, contentColor = Color.White)
        ) { Text("I'm ready", fontSize = 16.sp) }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun IntroPreview() { MaterialTheme { IntroScreen {} } }

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
private fun IntroSmallPreview() { MaterialTheme { IntroScreen {} } }
