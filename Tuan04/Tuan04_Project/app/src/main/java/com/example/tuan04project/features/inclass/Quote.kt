package com.example.tuan04project.features.inclass

import java.text.NumberFormat
import java.util.Locale

const val SO_LUONG = 1_000_000
const val NOI_DUNG = "The only way to do great work is to love what you do."

fun dinhDangSo(soThuTu: Int): String {
    return if (soThuTu < 10) {
        "0$soThuTu"
    } else {
        NumberFormat.getIntegerInstance(Locale.GERMANY).format(soThuTu)
    }
}
