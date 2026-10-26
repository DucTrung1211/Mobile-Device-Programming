package com.example.tuan04project.features.library.model

abstract class LibraryUser(
    val id: Int,
    val name: String
) {
    abstract fun getDescription(): String
}
