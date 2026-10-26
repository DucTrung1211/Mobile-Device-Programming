package com.example.tuan04project.features.library.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.tuan04project.features.library.data.InMemoryLibraryRepository
import com.example.tuan04project.features.library.data.LibraryRepository
import com.example.tuan04project.features.library.model.Book
import com.example.tuan04project.features.library.model.BorrowRecord
import com.example.tuan04project.features.library.model.Student

class LibraryViewModel(
    private val repository: LibraryRepository = InMemoryLibraryRepository()
) : ViewModel() {

    val books: List<Book>
        get() = repository.getBooks()

    val students: List<Student>
        get() = repository.getStudents()

    val borrowRecords: List<BorrowRecord>
        get() = repository.getBorrowRecords()

    var selectedStudentId by mutableStateOf(repository.getStudents().firstOrNull()?.id)
        private set

    private val selectedBookIds = mutableStateListOf<Int>()

    var message by mutableStateOf<String?>(null)
        private set

    val selectedStudent: Student?
        get() {
            val studentId = selectedStudentId ?: return null
            return repository.findStudent(studentId)
        }

    fun selectStudent(studentId: Int) {
        selectedStudentId = studentId
    }

    fun isBookSelected(bookId: Int): Boolean {
        return bookId in selectedBookIds
    }

    fun isBookBorrowed(bookId: Int): Boolean {
        return repository.isBookBorrowed(bookId)
    }

    fun toggleBookSelection(bookId: Int) {
        if (repository.isBookBorrowed(bookId)) {
            return
        }
        if (bookId in selectedBookIds) {
            selectedBookIds.remove(bookId)
        } else {
            selectedBookIds.add(bookId)
        }
    }

    fun borrowSelectedBooks() {
        val student = selectedStudent
        if (student == null) {
            message = "Vui lòng chọn sinh viên trước khi mượn sách"
            return
        }
        if (selectedBookIds.isEmpty()) {
            message = "Vui lòng chọn ít nhất một cuốn sách"
            return
        }

        var borrowedCount = 0
        for (bookId in selectedBookIds) {
            if (repository.borrowBook(student.id, bookId)) {
                borrowedCount++
            }
        }
        selectedBookIds.clear()

        message = if (borrowedCount > 0) {
            "${student.name} đã mượn $borrowedCount cuốn sách"
        } else {
            "Sách đã chọn hiện không thể cho mượn"
        }
    }

    fun returnBook(record: BorrowRecord) {
        if (repository.returnBook(record.id)) {
            message = "${getStudentName(record)} đã trả \"${getBookTitle(record)}\""
        }
    }

    fun addBook(title: String, author: String): Boolean {
        val isAdded = repository.addBook(title, author)
        message = if (isAdded) {
            "Đã thêm sách \"${title.trim()}\""
        } else {
            "Vui lòng nhập đầy đủ tên sách và tác giả"
        }
        return isAdded
    }

    fun addStudent(name: String): Boolean {
        val isAdded = repository.addStudent(name)
        message = if (isAdded) {
            "Đã thêm sinh viên ${name.trim()}"
        } else {
            "Tên sinh viên không được để trống"
        }
        return isAdded
    }

    fun getStudentName(record: BorrowRecord): String {
        return repository.findStudent(record.studentId)?.name ?: "Không rõ sinh viên"
    }

    fun getBookTitle(record: BorrowRecord): String {
        return repository.findBook(record.bookId)?.title ?: "Không rõ sách"
    }

    fun getBorrowerName(bookId: Int): String? {
        val record = repository.getBorrowRecords().find { it.bookId == bookId } ?: return null
        return getStudentName(record)
    }

    fun countBorrowedBooks(studentId: Int): Int {
        return repository.getBorrowRecords().count { it.studentId == studentId }
    }

    fun clearMessage() {
        message = null
    }
}
