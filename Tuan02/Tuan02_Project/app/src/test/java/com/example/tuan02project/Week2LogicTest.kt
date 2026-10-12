package com.example.tuan02project

import org.junit.Assert.assertEquals
import org.junit.Test

class Week2LogicTest {
    @Test
    fun classificationExamplesAndBoundaries() {
        assertEquals("Giỏi", classifyScore(8.5))
        assertEquals("Giỏi", classifyScore(9.0))
        assertEquals("Khá", classifyScore(7.0))
        assertEquals("Trung bình", classifyScore(5.5))
        assertEquals("Yếu", classifyScore(3.0))
        assertEquals("Không hợp lệ", classifyScore(11.0))
        assertEquals("Không hợp lệ", classifyScore(-0.1))
        assertEquals("Yếu", classifyScore(0.0))
        assertEquals("Trung bình", classifyScore(5.0))
        assertEquals("Khá", classifyScore(6.5))
        assertEquals("Giỏi", classifyScore(8.0))
        assertEquals("Giỏi", classifyScore(10.0))
        assertEquals("Không hợp lệ", classifyScore(Double.NaN))
    }

    @Test
    fun calculatorSupportsFourOperators() {
        assertEquals("8", calculateResult("4", "4", "+"))
        assertEquals("-2", calculateResult("2", "4", "-"))
        assertEquals("10", calculateResult("2.5", "4", "*"))
        assertEquals("2.5", calculateResult("10", "4", "/"))
        assertEquals("-1.5", calculateResult("-3", "2", "/"))
    }

    @Test
    fun calculatorHandlesMissingInvalidAndZeroInputs() {
        assertEquals("", calculateResult("", "4", "+"))
        assertEquals("", calculateResult("4", "", "+"))
        assertEquals("", calculateResult("abc", "4", "+"))
        assertEquals("Không thể chia cho 0", calculateResult("4", "0", "/"))
        assertEquals("Không thể chia cho 0", calculateResult("0", "-0.0", "/"))
        assertEquals("Số không hợp lệ", calculateResult("NaN", "4", "+"))
    }
}
