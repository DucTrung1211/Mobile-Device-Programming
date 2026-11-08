package com.uth.week6.permissions.model

import android.Manifest
import android.os.Build

enum class PermissionStep(val title: String, val description: String, val asset: String) {
    LOCATION("Location", "Allow maps to access your\nlocation while you use the app?", "location"),
    NOTIFICATION("Notification", "Please enable notifications to\nreceive updates and reminders", "notification"),
    CAMERA("Camera", "We need access to your camera to\nscan QR codes", "camera");

    fun permissions(sdk: Int = Build.VERSION.SDK_INT): Array<String> = when (this) {
        LOCATION -> arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        NOTIFICATION -> if (sdk >= 33) arrayOf(Manifest.permission.POST_NOTIFICATIONS) else emptyArray()
        CAMERA -> arrayOf(Manifest.permission.CAMERA)
    }
}
