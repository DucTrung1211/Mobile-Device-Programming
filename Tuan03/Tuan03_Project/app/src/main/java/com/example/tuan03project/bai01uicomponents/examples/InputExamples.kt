package com.example.tuan03project.bai01uicomponents.examples

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuan03project.bai01uicomponents.components

@Composable
fun TextFieldExample() {

    var noiDungNhap by rememberSaveable { mutableStateOf("") }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        OutlinedTextField(
            value = noiDungNhap,
            onValueChange = { noiDungNhap = it },
            placeholder = { Text("Thông tin nhập", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp), singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray, focusedBorderColor = Color.Gray)
        )
        Text(
            if (noiDungNhap.isEmpty()) "Tự động cập nhật dữ liệu theo textfield" else noiDungNhap,
            color = Color(0xFFB52A2A), fontSize = 14.sp
        )
    }
}

@Composable
fun PasswordExample() {
    var matKhau by rememberSaveable { mutableStateOf("") }
    var hienMatKhau by rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = matKhau, onValueChange = { matKhau = it },
        label = { Text("Mật khẩu") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (hienMatKhau) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            TextButton(onClick = { hienMatKhau = !hienMatKhau }) {
                Text(if (hienMatKhau) "Ẩn" else "Hiện")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputExample(id: String) {
    var noiDung by rememberSaveable { mutableStateOf("") }
    var moTimKiem by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        when (id) {
            "filled" -> TextField(value = noiDung, onValueChange = { noiDung = it },
                label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth())
            "outlined" -> OutlinedTextField(value = noiDung, onValueChange = { noiDung = it },
                label = { Text("Họ tên") }, modifier = Modifier.fillMaxWidth())
            "basicinput" -> BasicTextField(value = noiDung, onValueChange = { noiDung = it },
                modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, RoundedCornerShape(8.dp)).padding(16.dp),
                decorationBox = { oNhap ->
                    Box {
                        if (noiDung.isEmpty()) Text("Nhập bằng BasicTextField", color = Color.Gray)
                        oNhap()
                    }
                })
            "multiline" -> OutlinedTextField(value = noiDung, onValueChange = { noiDung = it },
                label = { Text("Ghi chú nhiều dòng") }, minLines = 3, maxLines = 6,
                modifier = Modifier.fillMaxWidth())
            "keyboard" -> OutlinedTextField(value = noiDung, onValueChange = { noiDung = it },
                label = { Text("Số lượng") }, keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth())
            "validation", "error" -> {
                val coLoi = noiDung.isNotEmpty() && (!noiDung.contains("@") || !noiDung.contains("."))
                OutlinedTextField(value = noiDung, onValueChange = { noiDung = it },
                    label = { Text("Email") }, isError = coLoi,
                    supportingText = { Text(if (coLoi) "Email cần có @ và dấu chấm" else "Ví dụ: sv@uth.edu.vn") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth())
                if (noiDung.isNotEmpty() && !coLoi) Text("Định dạng cơ bản hợp lệ", color = Color(0xFF217A35))
            }
            "search" -> {
                OutlinedTextField(value = noiDung, onValueChange = { noiDung = it },
                    label = { Text("Tìm component") }, modifier = Modifier.fillMaxWidth())
                for (ketQua in components.filter { it.name.contains(noiDung, ignoreCase = true) }.take(6)) {
                    Text(ketQua.name)
                }
                if (components.none { it.name.contains(noiDung, ignoreCase = true) }) Text("Không có kết quả")
            }
            "searchbar" -> SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(query = noiDung, onQueryChange = { noiDung = it },
                        onSearch = { moTimKiem = false },
                        expanded = moTimKiem, onExpandedChange = { moTimKiem = it },
                        placeholder = { Text("Tìm component") })
                },
                expanded = moTimKiem, onExpandedChange = { moTimKiem = it },
                modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp),
                windowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                val ketQua = components.filter { it.name.contains(noiDung, true) }.take(5)
                if (ketQua.isEmpty()) Text("Không có kết quả", Modifier.padding(16.dp))
                for (muc in ketQua) {
                    TextButton(onClick = { noiDung = muc.name; moTimKiem = false }) { Text(muc.name) }
                }
            }
        }
        if (id !in listOf("search", "searchbar", "validation", "error")) Text("Đang nhập: $noiDung")
        if (noiDung.isNotEmpty()) TextButton(onClick = { noiDung = "" }) { Text("Xóa dữ liệu") }
    }
}
