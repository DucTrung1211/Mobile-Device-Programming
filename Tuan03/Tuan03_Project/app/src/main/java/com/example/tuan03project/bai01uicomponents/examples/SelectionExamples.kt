package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun CheckboxExample() {
    var daChon by rememberSaveable { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = daChon, onCheckedChange = { daChon = it })
        Text(if (daChon) "Đã chọn" else "Chưa chọn")
    }
}

@Composable
fun RadioExample() {
    var luaChon by rememberSaveable { mutableStateOf("Kotlin") }
    Column(Modifier.selectableGroup()) {
        for (ten in listOf("Kotlin", "Java", "Dart")) {
            Row(Modifier.fillMaxWidth().selectable(
                selected = luaChon == ten, onClick = { luaChon = ten }, role = Role.RadioButton
            ).padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = luaChon == ten, onClick = null)
                Text(ten, Modifier.padding(start = 12.dp))
            }
        }
        Text("Đang chọn: $luaChon")
    }
}

@Composable
fun SwitchExample() {
    var daBat by rememberSaveable { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Switch(checked = daBat, onCheckedChange = { daBat = it })
        Text(if (daBat) "ON" else "OFF")
    }
}

@Composable
fun SliderExample() {
    var giaTriSlider by rememberSaveable { mutableFloatStateOf(50f) }
    Column {
        Slider(value = giaTriSlider, onValueChange = { giaTriSlider = it }, valueRange = 0f..100f)
        Text("Giá trị: ${giaTriSlider.toInt()}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionExample(id: String) {
    var daChon by rememberSaveable { mutableStateOf(false) }
    var luaChon by rememberSaveable { mutableIntStateOf(0) }
    var thongBao by rememberSaveable { mutableStateOf("Chưa nhấn") }
    var khoang by remember { mutableStateOf(20f..80f) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        when (id) {
            "range" -> {
                RangeSlider(value = khoang, onValueChange = { khoang = it }, valueRange = 0f..100f)
                Text("Từ ${khoang.start.toInt()} đến ${khoang.endInclusive.toInt()}")
            }
            "filterchip" -> {
                FilterChip(selected = daChon, onClick = { daChon = !daChon }, label = { Text("Chỉ hiện đã chọn") })
                Text(if (daChon) "Bộ lọc bật" else "Bộ lọc tắt")
            }
            "assistchip" -> {
                AssistChip(onClick = { thongBao = "Đã mở gợi ý học Kotlin" }, label = { Text("Hỗ trợ") })
                Text(thongBao)
            }
            "suggestionchip" -> {
                SuggestionChip(onClick = { thongBao = "Gợi ý đã chọn: Compose" }, label = { Text("Compose") })
                Text(thongBao)
            }
            "inputchip" -> {
                if (!daChon) InputChip(selected = true, onClick = { daChon = true }, label = { Text("Kotlin ×") })
                else TextButton(onClick = { daChon = false }) { Text("Thêm lại Kotlin") }
                Text(if (daChon) "Đã xóa Kotlin" else "Nhấn chip để xóa")
            }
            "segmented" -> {
                val cacMuc = listOf("Ngày", "Tuần", "Tháng")
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    cacMuc.forEachIndexed { viTri, ten ->
                        SegmentedButton(selected = luaChon == viTri, onClick = { luaChon = viTri },
                            shape = SegmentedButtonDefaults.itemShape(viTri, cacMuc.size)) { Text(ten) }
                    }
                }
                Text("Đã chọn: ${cacMuc[luaChon]}")
            }
            "date", "datedialog" -> DateExample(inDialog = id == "datedialog")
            "time" -> {
                val gio = rememberTimePickerState(initialHour = 9, initialMinute = 30, is24Hour = true)
                TimePicker(state = gio, modifier = Modifier.horizontalScroll(rememberScrollState()).width(300.dp))
                Text("Đã chọn: %02d:%02d".format(gio.hour, gio.minute))
            }
        }
    }
}

fun formatDate(millis: Long?): String {
    if (millis == null) return "Chưa chọn ngày"
    val dinhDang = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    dinhDang.timeZone = TimeZone.getTimeZone("UTC")
    return dinhDang.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateExample(inDialog: Boolean) {
    val ngay = rememberDatePickerState()
    var moHopThoai by rememberSaveable { mutableStateOf(false) }
    var ngayXacNhan by rememberSaveable { mutableStateOf<Long?>(null) }
    if (!inDialog) {
        DatePicker(state = ngay, showModeToggle = true,
            modifier = Modifier.horizontalScroll(rememberScrollState()).width(360.dp))
        Text("Đã chọn: ${formatDate(ngay.selectedDateMillis)}")
    } else {
        Button(onClick = { moHopThoai = true }) { Text("Chọn ngày") }
        Text("Đã xác nhận: ${formatDate(ngayXacNhan)}")
        if (moHopThoai) DatePickerDialog(
            onDismissRequest = { moHopThoai = false },
            confirmButton = {
                TextButton(enabled = ngay.selectedDateMillis != null, onClick = {
                    ngayXacNhan = ngay.selectedDateMillis
                    moHopThoai = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { moHopThoai = false }) { Text("Hủy") } }
        ) { DatePicker(state = ngay) }
    }
}
