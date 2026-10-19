package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tuan03project.R

@Composable
fun ActionExample(id: String) {
    var soLanNhan by rememberSaveable { mutableIntStateOf(0) }
    val khiNhan = { soLanNhan++; Unit }
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        when (id) {
            "button" -> Button(onClick = khiNhan) { Text("Nhấn Button") }
            "outlinedbutton" -> OutlinedButton(onClick = khiNhan) { Text("Nhấn OutlinedButton") }
            "textbutton" -> TextButton(onClick = khiNhan) { Text("Nhấn TextButton") }
            "elevatedbutton" -> ElevatedButton(onClick = khiNhan) { Text("Nhấn ElevatedButton") }
            "tonalbutton" -> FilledTonalButton(onClick = khiNhan) { Text("Nhấn FilledTonalButton") }
            "iconbutton" -> IconButton(onClick = khiNhan) {
                Icon(painterResource(R.drawable.ic_launcher), "Tăng số lần nhấn")
            }
            "fab" -> FloatingActionButton(onClick = khiNhan) { Text("+") }
            "extendedfab" -> ExtendedFloatingActionButton(
                onClick = khiNhan, icon = { Text("+") }, text = { Text("Thêm") })
        }
        Text("Đã nhấn $soLanNhan lần")
    }
}
