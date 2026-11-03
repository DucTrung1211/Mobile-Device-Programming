package com.example.tuan05project.navigation

/** Week 05 exercises shown on the picker; each keeps its own flow and back stack. */
enum class Lesson(val code: String, val title: String) {
    PICKER("", "Chọn bài"),
    AUTH("Bài 01", "Firebase Authentication"),
    FIREBASE("Bài 02", "Tích hợp dịch vụ Firebase"),
    LEGACY("Bài cũ", "Login by Gmail")
}

enum class Destination { LESSONS, RESTORING, LOGIN, HOME, PROFILE, SERVICES, LEGACY }

fun destinationFor(lesson: Lesson, restoring: Boolean, authenticated: Boolean, guest: Boolean, requested: String): Destination = when {
    lesson == Lesson.PICKER -> Destination.LESSONS
    lesson == Lesson.LEGACY -> Destination.LEGACY
    restoring -> Destination.RESTORING
    !authenticated && !guest -> Destination.LOGIN
    requested == "profile" -> Destination.PROFILE
    requested == "services" && lesson == Lesson.FIREBASE && authenticated -> Destination.SERVICES
    else -> Destination.HOME
}
