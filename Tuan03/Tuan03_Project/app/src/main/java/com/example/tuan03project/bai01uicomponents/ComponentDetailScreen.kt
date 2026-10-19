package com.example.tuan03project.bai01uicomponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan03project.bai01uicomponents.examples.*

@Composable
fun DetailHeader(title: String, onBack: (() -> Unit)? = null) {
    Box(Modifier.fillMaxWidth().height(56.dp), contentAlignment = Alignment.Center) {
        if (onBack != null) {
            TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)
                .semantics { contentDescription = "Quay lại" }) {
                Text("‹", color = TitleBlue, fontSize = 30.sp)
            }
        }
        Text(title, Modifier.padding(horizontal = if (onBack == null) 12.dp else 56.dp),
            color = TitleBlue, fontSize = 20.sp, fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
fun ComponentDetailScreen(componentId: String, onBack: () -> Unit) {
    val component = components.first { it.id == componentId }
    val tieuDe = when (componentId) {
        "text" -> "Text Detail"
        "image" -> "Images"
        "row" -> "Row Layout"
        "column" -> "Column Layout"
        "box" -> "Box Layout"
        else -> component.name
    }
    Column(Modifier.fillMaxSize()) {
        DetailHeader(tieuDe, onBack)
        val canGiua = componentId in listOf("text", "textfield", "password", "column", "box")
        Column(
            Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = if (canGiua) Arrangement.Center else Arrangement.Top
        ) {
            when (componentId) {
                "text" -> TextExample()
                "image" -> ImageExample()
                "textfield" -> TextFieldExample()
                "password" -> PasswordExample()
                "row" -> RowExample()
                "column" -> ColumnExample()
                "box" -> BoxExample()
                "checkbox" -> CheckboxExample()
                "radio" -> RadioExample()
                "switch" -> SwitchExample()
                "slider" -> SliderExample()
                else -> when (component.group) {
                    "Layout" -> LayoutExample(componentId)
                    "Display · More" -> DisplayExample(componentId)
                    "Input · More" -> InputExample(componentId)
                    "Selection" -> SelectionExample(componentId)
                    "Actions" -> ActionExample(componentId)
                    "Content" -> ContentExample(componentId)
                    "Navigation" -> NavigationExample(componentId)
                    "Feedback" -> if (componentId == "error") InputExample("error") else FeedbackExample(componentId)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun TextDetailPreview() { MaterialTheme { ComponentDetailScreen("text") {} } }

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
private fun RowSmallPreview() { MaterialTheme { ComponentDetailScreen("row") {} } }

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ImagesPreview() { MaterialTheme { ComponentDetailScreen("image") {} } }
