package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tuan03project.bai01uicomponents.ItemBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentExample(id: String) {
    var dangMo by rememberSaveable { mutableStateOf(false) }
    var luaChon by rememberSaveable { mutableStateOf("Kotlin") }
    var soThongBao by rememberSaveable { mutableIntStateOf(3) }
    var thongBao by rememberSaveable { mutableStateOf("Chưa xác nhận") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        when (id) {
            "card" -> Card(colors = CardDefaults.cardColors(containerColor = ItemBlue)) {
                Text("Card · Bài học Compose", Modifier.padding(24.dp))
            }
            "elevatedcard" -> ElevatedCard(elevation = CardDefaults.elevatedCardElevation(8.dp)) {
                Text("ElevatedCard · Có bóng đổ", Modifier.padding(24.dp))
            }
            "outlinedcard" -> OutlinedCard { Text("OutlinedCard · Có đường viền", Modifier.padding(24.dp)) }
            "listitem" -> ListItem(
                headlineContent = { Text("Kotlin") }, supportingContent = { Text("Ngôn ngữ dùng trong bài") },
                leadingContent = { Text("K") }, trailingContent = { Text("01") })
            "hdivider" -> {
                Text("Phần trên")
                HorizontalDivider()
                Text("Phần dưới")
            }
            "vdivider" -> Row(Modifier.height(80.dp), horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Text("Trái")
                VerticalDivider()
                Text("Phải")
            }
            "badge" -> {
                BadgedBox(badge = { if (soThongBao > 0) Badge { Text("$soThongBao") } }) { Text("Hộp thư") }
                Button(onClick = { soThongBao++ }) { Text("Thêm thông báo") }
                TextButton(onClick = { soThongBao = 0 }) { Text("Đánh dấu đã đọc") }
            }
            "dropdown" -> {
                Box {
                    Button(onClick = { dangMo = true }) { Text("Chọn: $luaChon") }
                    DropdownMenu(expanded = dangMo, onDismissRequest = { dangMo = false }) {
                        for (ten in listOf("Kotlin", "Java", "Dart")) {
                            DropdownMenuItem(text = { Text(ten) }, onClick = { luaChon = ten; dangMo = false })
                        }
                    }
                }
                Text("Đã chọn: $luaChon")
            }
            "exposed" -> ExposedDropdownMenuBox(
                expanded = dangMo, onExpandedChange = { dangMo = it }
            ) {
                OutlinedTextField(value = luaChon, onValueChange = {}, readOnly = true,
                    label = { Text("Ngôn ngữ") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dangMo) },
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth())
                ExposedDropdownMenu(expanded = dangMo, onDismissRequest = { dangMo = false }) {
                    for (ten in listOf("Kotlin", "Java", "Dart")) {
                        DropdownMenuItem(text = { Text(ten) }, onClick = { luaChon = ten; dangMo = false })
                    }
                }
            }
            "alert" -> {
                Button(onClick = { dangMo = true }) { Text("Mở AlertDialog") }
                Text(thongBao)
                if (dangMo) AlertDialog(
                    onDismissRequest = { dangMo = false }, title = { Text("Lưu bài?") },
                    text = { Text("Ví dụ xác nhận, không ghi dữ liệu ra ngoài.") },
                    confirmButton = { TextButton(onClick = { thongBao = "Đã xác nhận"; dangMo = false }) { Text("Đồng ý") } },
                    dismissButton = { TextButton(onClick = { dangMo = false }) { Text("Hủy") } })
            }
            "dialog" -> {
                Button(onClick = { dangMo = true }) { Text("Mở Dialog") }
                if (dangMo) Dialog(onDismissRequest = { dangMo = false }) {
                    Surface(shape = RoundedCornerShape(16.dp)) {
                        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text("Nội dung Dialog tùy chỉnh")
                            Button(onClick = { dangMo = false }) { Text("Đóng") }
                        }
                    }
                }
            }
            "sheet" -> {
                Button(onClick = { dangMo = true }) { Text("Mở Bottom Sheet") }
                Text("Đã chọn: $luaChon")
                if (dangMo) ModalBottomSheet(onDismissRequest = { dangMo = false }) {
                    Column(Modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding()) {
                        Text("Chọn ngôn ngữ")
                        for (ten in listOf("Kotlin", "Java", "Dart")) {
                            ListItem(headlineContent = { Text(ten) },
                                modifier = Modifier.clickable { luaChon = ten; dangMo = false })
                        }
                    }
                }
            }
            "scaffold" -> Scaffold(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                topBar = { TopAppBar(title = { Text("Scaffold demo") }, windowInsets = WindowInsets(0, 0, 0, 0)) },
                floatingActionButton = { FloatingActionButton(onClick = { soThongBao++ }) { Text("+") } },
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) { khoangTrong ->
                Text("Nội dung nhận padding: $soThongBao", Modifier.padding(khoangTrong).padding(16.dp))
            }
        }
    }
}
