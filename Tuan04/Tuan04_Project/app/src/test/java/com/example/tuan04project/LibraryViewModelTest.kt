package com.example.tuan04project

import com.example.tuan04project.features.library.data.InMemoryLibraryRepository
import com.example.tuan04project.features.library.viewmodel.LibraryViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryViewModelTest {
    @Test
    fun borrowSelectedBooksCreatesRecordsForSelectedStudent() {
        val viewModel = LibraryViewModel(InMemoryLibraryRepository())
        val student = viewModel.students[1]
        val firstBook = viewModel.books[0]
        val secondBook = viewModel.books[1]

        viewModel.selectStudent(student.id)
        viewModel.toggleBookSelection(firstBook.id)
        viewModel.toggleBookSelection(secondBook.id)
        viewModel.borrowSelectedBooks()

        assertEquals(2, viewModel.borrowRecords.size)
        assertEquals(student.name, viewModel.getStudentName(viewModel.borrowRecords[0]))
        assertEquals(firstBook.title, viewModel.getBookTitle(viewModel.borrowRecords[0]))
        assertEquals(student.name, viewModel.getBorrowerName(secondBook.id))
        assertFalse(viewModel.isBookSelected(firstBook.id))
    }

    @Test
    fun borrowWithoutSelectedBooksCreatesNoRecord() {
        val viewModel = LibraryViewModel(InMemoryLibraryRepository())
        viewModel.borrowSelectedBooks()

        assertTrue(viewModel.borrowRecords.isEmpty())
        assertEquals("Vui lòng chọn ít nhất một cuốn sách", viewModel.message)
    }

    @Test
    fun borrowedBookCannotBeBorrowedAgainUntilReturned() {
        val repository = InMemoryLibraryRepository()
        val book = repository.getBooks()[0]
        val firstStudent = repository.getStudents()[0]
        val secondStudent = repository.getStudents()[1]

        assertTrue(repository.borrowBook(firstStudent.id, book.id))
        assertFalse(repository.borrowBook(secondStudent.id, book.id))

        val record = repository.getBorrowRecords()[0]
        assertTrue(repository.returnBook(record.id))
        assertTrue(repository.borrowBook(secondStudent.id, book.id))
    }

    @Test
    fun addBookAndStudentValidateInput() {
        val viewModel = LibraryViewModel(InMemoryLibraryRepository())
        val bookCount = viewModel.books.size
        val studentCount = viewModel.students.size

        assertFalse(viewModel.addBook("  ", "Tác giả"))
        assertTrue(viewModel.addBook("Android nâng cao", "Nguyễn B"))
        assertFalse(viewModel.addStudent(""))
        assertTrue(viewModel.addStudent("Pham Van D"))

        assertEquals(bookCount + 1, viewModel.books.size)
        assertEquals(studentCount + 1, viewModel.students.size)
        assertEquals("Pham Van D", viewModel.students.last().name)
        assertNull(viewModel.getBorrowerName(viewModel.books.last().id))
    }
}
