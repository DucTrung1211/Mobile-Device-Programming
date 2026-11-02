package com.example.tuan05project.data

import android.content.Context
import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.CancellationException

class AvatarRepository(private val tasks: FirebaseTaskRepository = FirebaseTaskRepository()) {
    private val storage get() = FirebaseStorage.getInstance()
    private fun avatar(uid: String) = storage.reference.child("users/$uid/avatar")
    fun versions(uid: String) = callbackFlow {
        val ref = tasks.database().getReference("users/$uid/profile/avatarVersion")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { trySend(snapshot.getValue(Long::class.java)) }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }
    suspend fun read(uid: String): ByteArray = withTimeout(20_000) { avatar(uid).getBytes(5L * 1024 * 1024).await() }
    suspend fun upload(context: Context, uid: String, uri: Uri, progress: (Float) -> Unit): ByteArray {
        val mime = context.contentResolver.getType(uri)
        require(mime in listOf("image/jpeg", "image/png", "image/webp")) { "Chọn ảnh JPEG, PNG hoặc WebP." }
        val bytes = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { it.readBytesLimited() }
                ?: error("Không đọc được ảnh.")
        }
        val task = avatar(uid).putBytes(bytes, StorageMetadata.Builder().setContentType(mime).build())
        task.addOnProgressListener { progress(if (it.totalByteCount > 0) it.bytesTransferred.toFloat() / it.totalByteCount else 0f) }
        try { withTimeout(60_000) { task.await() } }
        catch (error: CancellationException) { task.cancel(); throw error }
        withTimeout(20_000) { tasks.database().getReference("users/$uid/profile/avatarVersion").setValue(ServerValue.TIMESTAMP).await() }
        return bytes
    }
    private fun java.io.InputStream.readBytesLimited(): ByteArray {
        val out = java.io.ByteArrayOutputStream()
        val buffer = ByteArray(8192)
        while (true) {
            val n = read(buffer)
            if (n < 0) break
            require(out.size() + n <= 5 * 1024 * 1024) { "Ảnh phải nhỏ hơn hoặc bằng 5 MiB." }
            out.write(buffer, 0, n)
        }
        return out.toByteArray()
    }
}
