package com.example.tuan02project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuan02project.ui.theme.Tuan02_ProjectTheme

fun calculateResult(firstNumber: String, secondNumber: String, selectedOperator: String): String {
    val number1 = firstNumber.toDoubleOrNull()
    val number2 = secondNumber.toDoubleOrNull()
    if (number1 == null || number2 == null) return ""
    if (!number1.isFinite() || !number2.isFinite()) return "Số không hợp lệ"
    if (selectedOperator == "/" && number2 == 0.0) return "Không thể chia cho 0"

    val result = if (selectedOperator == "+") {
        number1 + number2
    } else if (selectedOperator == "-") {
        number1 - number2
    } else if (selectedOperator == "*") {
        number1 * number2
    } else {
        number1 / number2
    }
    return result.toString().removeSuffix(".0")
}

@Composable
fun ThucHanh03Screen() {
    var firstNumber by remember { mutableStateOf("") }
    var secondNumber by remember { mutableStateOf("") }
    var selectedOperator by remember { mutableStateOf("+") }
    val result = calculateResult(firstNumber, secondNumber, selectedOperator)

    Column(
        Modifier.fillMaxSize().background(Color.White).safeDrawingPadding()
            .imePadding().verticalScroll(rememberScrollState())
    ) {
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(140.dp))
            Text("Thực hành 03", modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Black)
            Spacer(Modifier.height(72.dp))
            OutlinedTextField(
                value = firstNumber,
                onValueChange = { firstNumber = it },
                placeholder = { Text("Số thứ nhất") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(24.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val operators = listOf("+", "-", "*", "/")
                for (operator in operators) {
                    val color = if (operator == "+") Color(0xFFCE4B43)
                    else if (operator == "-") Color(0xFFDCAF4E)
                    else if (operator == "*") Color(0xFF623BDC)
                    else Color(0xFF202020)
                    Button(
                        onClick = { selectedOperator = operator },
                        modifier = Modifier.size(52.dp),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color, contentColor = Color.White),
                        border = if (selectedOperator == operator)
                            BorderStroke(3.dp, Color.Black) else null
                    ) {
                        Text(if (operator == "*") "×" else operator)
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = secondNumber,
                onValueChange = { secondNumber = it },
                placeholder = { Text("Số thứ hai") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text("Kết quả: $result", color = Color.Black)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThucHanh03Preview() {
    Tuan02_ProjectTheme { ThucHanh03Screen() }
}
