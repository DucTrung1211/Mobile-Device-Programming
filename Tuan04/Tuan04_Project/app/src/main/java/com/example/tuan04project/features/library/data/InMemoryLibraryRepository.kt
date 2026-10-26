package com.example.tuan04project.features.library.data

import androidx.compose.runtime.mutableStateListOf
import com.example.tuan04project.features.library.model.Book
import com.example.tuan04project.features.library.model.BorrowRecord
import com.example.tuan04project.features.library.model.Student
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InMemoryLibraryRepository : LibraryRepository {
    private val books = mutableStateListOf<Book>()
    private val students = mutableStateListOf<Student>()
    private val borrowRecords = mutableStateListOf<BorrowRecord>()

    private var nextBookId = 1
    private var nextStudentId = 1
    private var nextRecordId = 1

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        addBook("Lập trình Kotlin cơ bản", "Nguyễn Văn Hùng")
        addBook("Jetpack Compose cho người mới", "Trần Minh Quân")
        addBook("Cấu trúc dữ liệu và giải thuật", "Lê Thị Mai")
        addBook("Cơ sở dữ liệu", "Phạm Đức Long")
        addBook("Mạng máy tính", "Võ Thanh Tùng")

        addStudent("Nguyen Van A")
        addStudent("Tran Thi B")
        addStudent("Le Van C")
    }

    override fun getBooks(): List<Book> = books

    override fun getStudents(): List<Student> = students

    override fun getBorrowRecords(): List<BorrowRecord> = borrowRecords

    override fun findBook(bookId: Int): Book? {
        return books.find { book -> book.id == bookId }
    }

    override fun findStudent(studentId: Int): Student? {
        return students.find { student -> student.id == studentId }
    }

    override fun isBookBorrowed(bookId: Int): Boolean {
        return borrowRecords.any { record -> record.bookId == bookId }
    }

    override fun addBook(title: String, author: String): Boolean {
        val cleanTitle = title.trim()
        val cleanAuthor = author.trim()
        if (cleanTitle.isEmpty() || cleanAuthor.isEmpty()) {
            return false
        }
        books.add(Book(id = nextBookId, title = cleanTitle, author = cleanAuthor))
        nextBookId++
        return true
    }

    override fun addStudent(name: String): Boolean {
        val cleanName = name.trim()
        if (cleanName.isEmpty()) {
            return false
        }
        val studentCode = "SV" + nextStudentId.toString().padStart(3, '0')
        students.add(Student(id = nextStudentId, name = cleanName, studentCode = studentCode))
        nextStudentId++
        return true
    }

    override fun borrowBook(studentId: Int, bookId: Int): Boolean {
        val studentExists = findStudent(studentId) != null
        val bookExists = findBook(bookId) != null
        if (!studentExists || !bookExists || isBookBorrowed(bookId)) {
            return false
        }
        val today = LocalDate.now().format(dateFormatter)
        borrowRecords.add(
            BorrowRecord(id = nextRecordId, studentId = studentId, bookId = bookId, borrowDate = today)
        )
        nextRecordId++
        return true
    }

    override fun returnBook(recordId: Int): Boolean {
        val record = borrowRecords.find { it.id == recordId } ?: return false
        borrowRecords.remove(record)
        return true
    }
}
