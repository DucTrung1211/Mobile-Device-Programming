package com.example.tuan04project

import com.example.tuan04project.features.todo.viewmodel.TodoViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TodoViewModelTest {
    @Test
    fun pagesMoveForwardAndBackInOrder() {
        val viewModel = TodoViewModel()
        assertEquals(3, viewModel.pages.size)
        assertEquals("Mọi việc, gọn trong một chỗ", viewModel.currentPage.title)

        viewModel.goToNextPage()
        assertEquals("Chia theo Học tập và Cuộc sống", viewModel.currentPage.title)
        assertFalse(viewModel.isLastPage)

        viewModel.goToNextPage()
        assertTrue(viewModel.isLastPage)
        assertEquals("Bắt đầu ngay", viewModel.currentPage.buttonText)

        viewModel.goToNextPage()
        assertEquals(2, viewModel.currentPageIndex)

        viewModel.goToPreviousPage()
        assertEquals(1, viewModel.currentPageIndex)
    }
}
