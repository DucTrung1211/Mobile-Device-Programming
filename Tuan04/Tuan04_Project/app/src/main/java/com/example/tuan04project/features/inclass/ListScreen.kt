package com.example.tuan04project.features.inclass

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan04project.R
import com.example.tuan04project.ui.theme.XanhDam
import com.example.tuan04project.ui.theme.XanhNhat

@Composable
fun ListScreen(onBack: () -> Unit, onItemClick: (Int) -> Unit) {
    val listState = rememberLazyListState()
    Column(Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()) {
        ScreenHeader(tieuDe = "LazyColumn", onBack = onBack)
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(count = SO_LUONG) { index ->
                val soThuTu = index + 1
                QuoteItem(
                    soThuTu = soThuTu,
                    noiDung = NOI_DUNG,
                    onClick = { onItemClick(soThuTu) }
                )
            }
        }
    }
}

@Composable
fun QuoteItem(soThuTu: Int, noiDung: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(XanhNhat)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp)
            .heightIn(min = 56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${dinhDangSo(soThuTu)} | $noiDung",
            modifier = Modifier.weight(1f),
            color = Color.Black,
            fontSize = 14.sp,
            lineHeight = 19.sp
        )
        Spacer(Modifier.width(12.dp))
        Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
            Box(
                Modifier.size(34.dp).background(XanhDam, RoundedCornerShape(5.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_chevron),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
