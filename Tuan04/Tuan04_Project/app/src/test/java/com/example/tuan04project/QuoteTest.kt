package com.example.tuan04project

import com.example.tuan04project.features.inclass.SO_LUONG
import com.example.tuan04project.features.inclass.dinhDangSo
import org.junit.Assert.assertEquals
import org.junit.Test

class QuoteTest {
    @Test
    fun formatFirstAndLargeItemNumbers() {
        assertEquals("01", dinhDangSo(1))
        assertEquals("09", dinhDangSo(9))
        assertEquals("10", dinhDangSo(10))
        assertEquals("1.000", dinhDangSo(1000))
        assertEquals("1.000.000", dinhDangSo(SO_LUONG))
    }
}
