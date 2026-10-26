package com.example.tuan04project.features.library.model

data class BorrowRecord(
    val id: Int,
    val studentId: Int,
    val bookId: Int,
    val borrowDate: String
)
