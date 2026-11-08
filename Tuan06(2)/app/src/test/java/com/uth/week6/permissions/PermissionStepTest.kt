package com.uth.week6.permissions

import android.Manifest
import com.uth.week6.permissions.model.PermissionStep
import org.junit.Assert.*
import org.junit.Test

class PermissionStepTest {
    @Test fun stepOrderMatchesAssignment() {
        assertEquals(listOf("Location", "Notification", "Camera"), PermissionStep.entries.map { it.title })
    }
    @Test fun locationRequestsFineAndCoarseTogether() {
        assertArrayEquals(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            PermissionStep.LOCATION.permissions(32))
    }
    @Test fun notificationRuntimeRequestOnlyStartsOn33() {
        assertTrue(PermissionStep.NOTIFICATION.permissions(32).isEmpty())
        assertArrayEquals(arrayOf(Manifest.permission.POST_NOTIFICATIONS), PermissionStep.NOTIFICATION.permissions(33))
    }
    @Test fun cameraRequestsRealManifestPermission() {
        assertArrayEquals(arrayOf(Manifest.permission.CAMERA), PermissionStep.CAMERA.permissions(32))
    }
}
