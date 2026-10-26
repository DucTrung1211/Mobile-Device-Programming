package com.example.tuan04project

import com.example.tuan04project.features.dataflow.viewmodel.DataFlowViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DataFlowViewModelTest {
    @Test
    fun invalidInputIsRejected() {
        val viewModel = DataFlowViewModel()

        viewModel.updateEmail("uth.gmail.com")
        assertFalse(viewModel.submitEmail())

        viewModel.updateOtp("12a")
        assertEquals("", viewModel.otp)
        viewModel.updateOtp("123")
        assertFalse(viewModel.submitOtp())

        viewModel.updateNewPassword("abc123")
        viewModel.updateConfirmPassword("abc124")
        assertFalse(viewModel.submitNewPassword())
        assertFalse(viewModel.submit())
        assertNull(viewModel.submittedResult)
    }

    @Test
    fun submitSendsAllDataBackAsResult() {
        val viewModel = DataFlowViewModel()

        viewModel.updateEmail(" uth@gmail.com ")
        assertTrue(viewModel.submitEmail())
        viewModel.updateOtp("12345")
        viewModel.updateOtp("123456")
        assertTrue(viewModel.submitOtp())
        viewModel.updateNewPassword("secret1")
        viewModel.updateConfirmPassword("secret1")
        assertTrue(viewModel.submitNewPassword())
        assertTrue(viewModel.submit())

        val result = viewModel.submittedResult
        assertEquals("uth@gmail.com", result?.email)
        assertEquals("12345", result?.otp)
        assertEquals("secret1", result?.password)
    }
}
