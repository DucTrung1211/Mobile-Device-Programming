package com.example.tuan03project.bai01uicomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ComponentsListScreen(listState: LazyListState, onComponentClick: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        DetailHeader("UI Components List")
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (nhom in components.map { it.group }.distinct()) {
                item(key = "group_$nhom") {
                    Text(nhom, Modifier.padding(top = 12.dp), fontWeight = FontWeight.Medium)
                }
                items(components.filter { it.group == nhom }, key = { it.id }) { component ->
                    ComponentItem(component) { onComponentClick(component.id) }
                }
            }
        }
    }
}

@Composable
fun ComponentItem(component: ComponentInfo, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            .background(ItemBlue).clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp)
    ) {
        Text(component.name, fontWeight = FontWeight.Bold)
        Text(component.description, style = MaterialTheme.typography.bodyMedium)
    }
}
