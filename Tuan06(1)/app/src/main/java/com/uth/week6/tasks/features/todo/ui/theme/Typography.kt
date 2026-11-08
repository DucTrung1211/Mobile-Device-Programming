package com.uth.week6.tasks.features.todo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import com.uth.week6.tasks.R

val TodoFontFamily = FontFamily(
    Font(R.font.be_vietnam_regular, FontWeight.Normal),
    Font(R.font.be_vietnam_medium, FontWeight.Medium),
    Font(R.font.be_vietnam_semibold, FontWeight.SemiBold),
    Font(R.font.be_vietnam_bold, FontWeight.Bold)
)
private val base = Typography()
val TodoTypography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    displayMedium = base.displayMedium.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    displaySmall = base.displaySmall.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    headlineLarge = base.headlineLarge.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    headlineMedium = base.headlineMedium.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    headlineSmall = base.headlineSmall.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    titleLarge = base.titleLarge.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    titleMedium = base.titleMedium.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    titleSmall = base.titleSmall.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    bodyLarge = base.bodyLarge.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    bodyMedium = base.bodyMedium.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    bodySmall = base.bodySmall.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    labelLarge = base.labelLarge.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    labelMedium = base.labelMedium.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp),
    labelSmall = base.labelSmall.copy(fontFamily = TodoFontFamily, lineHeight = TextUnit.Unspecified, letterSpacing = 0.sp)
)
