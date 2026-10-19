package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tuan03project.bai01uicomponents.*

@Composable
fun RowExample() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        repeat(4) {
            Row(
                Modifier.fillMaxWidth().background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
                    Box(Modifier.weight(1f).height(64.dp).background(mau, RoundedCornerShape(12.dp)))
                }
            }
        }
    }
}

@Composable
fun ColumnExample() {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (mau in listOf(LightTileBlue, DarkTileBlue, LightTileBlue)) {
            Box(Modifier.width(150.dp).height(64.dp).background(mau, RoundedCornerShape(12.dp)))
        }
    }
}

@Composable
fun BoxExample() {
    Box(Modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Box(Modifier.offset(x = (-55).dp, y = (-55).dp).size(90.dp)
            .background(Color(0xFFFFB5A2), RoundedCornerShape(10.dp)))
        Box(Modifier.offset(x = 55.dp, y = 55.dp).size(90.dp)
            .background(Color(0xFFFFB5A2), RoundedCornerShape(10.dp)))
        Box(Modifier.size(120.dp).background(Color(0xFFFF653B), RoundedCornerShape(10.dp)))
    }
}

@Composable
fun NumberTile(number: Int, modifier: Modifier = Modifier) {
    Box(modifier.background(ItemBlue, RoundedCornerShape(8.dp)).padding(18.dp),
        contentAlignment = Alignment.Center) { Text("Ô $number") }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LayoutExample(id: String) {
    when (id) {
        "spacer" -> Column {
            Text("Phía trên")
            Spacer(Modifier.height(64.dp))
            Text("Phía dưới · Spacer 64.dp")
        }
        "lazycolumn" -> LazyColumn(Modifier.fillMaxWidth().height(340.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(30) { NumberTile(it + 1, Modifier.fillMaxWidth()) }
        }
        "lazyrow" -> LazyRow(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(20) { NumberTile(it + 1) }
        }
        "vgrid" -> LazyVerticalGrid(GridCells.Fixed(3), Modifier.height(340.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(36) { NumberTile(it + 1) }
        }
        "hgrid" -> LazyHorizontalGrid(GridCells.Fixed(2), Modifier.fillMaxWidth().height(200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(30) { NumberTile(it + 1) }
        }
        "flowrow" -> FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(9) { NumberTile(it + 1) }
        }
        "flowcolumn" -> FlowColumn(Modifier.fillMaxWidth().height(230.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(6) { NumberTile(it + 1) }
        }
        "constraints" -> BoxWithConstraints(Modifier.fillMaxWidth()) {
            val chieuRong = maxWidth
            Column {
                Text("Chiều rộng khả dụng: $chieuRong")
                if (chieuRong < 360.dp) ColumnExample() else RowExample()
            }
        }
        "arrangement" -> {
            var gianDeu by remember { mutableStateOf(false) }
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Switch(checked = gianDeu, onCheckedChange = { gianDeu = it })
                Text(if (gianDeu) "SpaceBetween" else "Center")
                Row(Modifier.fillMaxWidth().height(100.dp).background(Color(0xFFF3F3F3)),
                    horizontalArrangement = if (gianDeu) Arrangement.SpaceBetween else Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {
                    repeat(3) { NumberTile(it + 1, Modifier.width(76.dp)) }
                }
            }
        }
        "modifiers" -> {
            var khoangCach by remember { mutableFloatStateOf(16f) }
            Column {
                Slider(value = khoangCach, onValueChange = { khoangCach = it }, valueRange = 0f..40f)
                Text("Padding: ${khoangCach.toInt()} dp")
                Box(Modifier.fillMaxWidth().border(2.dp, DarkTileBlue).padding(khoangCach.dp)
                    .background(ItemBlue).height(100.dp), contentAlignment = Alignment.Center) { Text("Nội dung") }
            }
        }
    }
}
