package com.example.tuan04project.features.library.model

class Student(
    id: Int,
    name: String,
    val studentCode: String
) : LibraryUser(id, name) {

    override fun getDescription(): String {
        return "Sinh viên • Mã SV: $studentCode"
    }
}
