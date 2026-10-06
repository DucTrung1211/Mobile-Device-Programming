package com.example.tuan01profile.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan01profile.R
import com.example.tuan01profile.ui.theme.Tuan01_ProfileTheme

private const val STUDENT_NAME = "Trần Đức Trung"
private const val STUDENT_ID = "001206004489"

@Composable
fun ProfileScreen(onBackClick: () -> Unit = {}) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .safeDrawingPadding()
    ) {
        val avatarBackgroundSize = minOf(184.dp, maxWidth * 0.52f, maxHeight * 0.40f)
        val profileOffset = maxHeight * -0.035f

        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProfileIconButton(
                iconResId = R.drawable.ic_profile_back,
                description = "Quay lại",
                tint = Color(0xFF222222),
                onClick = onBackClick
            )
            ProfileIconButton(
                iconResId = R.drawable.ic_profile_edit,
                description = "Chỉnh sửa",
                tint = Color(0xFF55B9AD),
                onClick = {}
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = profileOffset)
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(avatarBackgroundSize)
                    .clip(CircleShape)
                    .background(Color(0xFFDCEFF7)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.avatar_tran_duc_trung_cutout),
                    contentDescription = "Ảnh đại diện $STUDENT_NAME",
                    contentScale = ContentScale.Crop,
                    alignment = BiasAlignment(0f, -0.75f),
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = STUDENT_NAME,
                color = Color(0xFF111111),
                fontSize = 24.sp,
                lineHeight = 29.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = STUDENT_ID,
                color = Color(0xFF777777),
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ProfileIconButton(
    @DrawableRes iconResId: Int,
    description: String,
    tint: Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(42.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFDDDDDD))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = description,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(
    name = "Profile - Portrait",
    showBackground = true,
    showSystemUi = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
private fun ProfileScreenPreview() {
    Tuan01_ProfileTheme(darkTheme = false, dynamicColor = false) {
        ProfileScreen()
    }
}
