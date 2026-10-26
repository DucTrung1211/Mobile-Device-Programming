package com.example.tuan04project.features.inclass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.ui.theme.XanhDuong

@Composable
fun ScreenHeader(tieuDe: String, onBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                painter = painterResource(R.drawable.ic_chevron),
                contentDescription = "Quay lại",
                tint = XanhDuong,
                modifier = Modifier.size(24.dp).graphicsLayer { rotationZ = 180f }
            )
        }
        Text(
            text = tieuDe,
            modifier = Modifier.padding(horizontal = 56.dp),
            color = XanhDuong,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
