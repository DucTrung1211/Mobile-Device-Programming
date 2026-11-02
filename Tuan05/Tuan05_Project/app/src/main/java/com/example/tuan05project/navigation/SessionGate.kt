package com.example.tuan05project.navigation

enum class Destination { LESSONS, LOGIN, HOME, PROFILE, SERVICES, LEGACY }
fun destinationFor(lesson: Int, authenticated: Boolean, guest: Boolean, requested: String): Destination = when {
    lesson == 0 -> Destination.LESSONS
    lesson == 3 -> Destination.LEGACY
    !authenticated && !guest -> Destination.LOGIN
    requested == "profile" -> Destination.PROFILE
    requested == "services" && lesson == 2 && authenticated -> Destination.SERVICES
    else -> Destination.HOME
}
