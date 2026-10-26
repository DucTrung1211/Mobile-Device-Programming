package com.example.tuan04project.features.library.data

import com.example.tuan04project.features.library.model.Book
import com.example.tuan04project.features.library.model.BorrowRecord
import com.example.tuan04project.features.library.model.Student

interface LibraryRepository {
    fun getBooks(): List<Book>
    fun getStudents(): List<Student>
    fun getBorrowRecords(): List<BorrowRecord>

    fun findBook(bookId: Int): Book?
    fun findStudent(studentId: Int): Student?
    fun isBookBorrowed(bookId: Int): Boolean

    fun addBook(title: String, author: String): Boolean
    fun addStudent(name: String): Boolean
    fun borrowBook(studentId: Int, bookId: Int): Boolean
    fun returnBook(recordId: Int): Boolean
}
