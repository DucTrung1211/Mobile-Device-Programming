package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan03project.R
import com.example.tuan03project.bai01uicomponents.ItemBlue

@Composable
fun TextExample() {
    val cauVan = buildAnnotatedString {
        append("The ")
        withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) { append("quick") }
        append(" ")
        withStyle(SpanStyle(color = Color(0xFFB87818), fontSize = 38.sp)) { append("Brown") }
        append("\nfox j u m p s  ")
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append("over") }
        append("\n")
        withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) { append("the") }
        append(" ")
        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) { append("lazy") }
        append(" dog.")
    }
    Column(Modifier.fillMaxWidth()) {
        Text(cauVan, Modifier.padding(horizontal = 12.dp), fontSize = 30.sp, lineHeight = 56.sp)
        Spacer(Modifier.height(18.dp))
        HorizontalDivider(color = Color(0xFFE6E6E6))
    }
}

@Composable
fun ImageExample() {
    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painterResource(R.drawable.uth_campus_one), "Mặt trước trường UTH",
            Modifier.fillMaxWidth().aspectRatio(1.78f).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
        Text("commons.wikimedia.org/wiki/File:Ảnh_Mặt_Trước_UTH.jpg",
            fontSize = 12.sp, textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline)
        Spacer(Modifier.height(28.dp))
        Image(
            painterResource(R.drawable.uth_campus_two), "Một góc khác của trường UTH",
            Modifier.fillMaxWidth().aspectRatio(1.78f).clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
        Text("In app", fontSize = 14.sp)
    }
}

@Composable
fun DisplayExample(id: String) {
    when (id) {
        "basictext" -> BasicText("BasicText: văn bản Foundation, không áp dụng kiểu chữ Material tự động.")
        "styled" -> TextExample()
        "icon" -> Icon(painterResource(R.drawable.ic_launcher), "Biểu tượng ứng dụng",
            Modifier.size(72.dp), tint = Color.Unspecified)
        "surface" -> Surface(shape = RoundedCornerShape(16.dp), color = ItemBlue,
            tonalElevation = 4.dp) { Text("Nội dung bên trong Surface", Modifier.padding(24.dp)) }
        "imagestyle" -> Image(
            painterResource(R.drawable.uth_campus_one), "Ảnh UTH được bo tròn",
            Modifier.size(180.dp).clip(RoundedCornerShape(90.dp)), contentScale = ContentScale.Crop)
        "textstyles" -> Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
            Text("Văn bản rất dài để thấy dấu ba chấm khi nội dung vượt chiều rộng.",
                Modifier.width(180.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text("Căn giữa", Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Text("Căn phải", Modifier.fillMaxWidth(), textAlign = TextAlign.End)
            Text("Gạch chân", textDecoration = TextDecoration.Underline)
            Text("Gạch ngang", textDecoration = TextDecoration.LineThrough)
        }
    }
}
