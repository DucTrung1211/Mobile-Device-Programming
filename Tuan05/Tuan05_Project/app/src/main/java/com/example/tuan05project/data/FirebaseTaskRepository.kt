package com.example.tuan05project.data

import com.example.tuan05project.features.todo.model.*
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.time.LocalDate
import java.time.LocalTime

interface TaskRepository {
    fun observe(uid: String): Flow<List<Task>>
    suspend fun save(uid: String, id: String?, draft: TaskDraft)
    suspend fun toggle(uid: String, task: Task)
    suspend fun delete(uid: String, id: String)
}

class FirebaseTaskRepository : TaskRepository {
    fun database(): FirebaseDatabase {
        val app = FirebaseApp.getInstance()
        val id = app.applicationContext.resources.getIdentifier("todo_database_url", "string", app.applicationContext.packageName)
        val override = if (id == 0) "" else app.applicationContext.getString(id).trim()
        val url = override.ifEmpty { app.options.databaseUrl.orEmpty() }
        check(url.isNotBlank()) { "Chưa cấu hình Realtime Database. Thêm URL thật vào todo_database_url hoặc cập nhật google-services.json." }
        return FirebaseDatabase.getInstance(app, url)
    }
    private fun tasks(uid: String) = database().reference.child("users").child(uid).child("tasks")
    override fun observe(uid: String): Flow<List<Task>> = callbackFlow {
        val reference = tasks(uid)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    trySend(snapshot.children.map { child ->
                        TaskCodec.decode(checkNotNull(child.key), child.value as? Map<*, *> ?: error("Dữ liệu công việc không hợp lệ"))
                    }.sortedWith(compareBy<Task> { it.date }.thenBy { it.time }))
                } catch (_: Exception) { close(IllegalStateException("Có công việc sai định dạng trên máy chủ.")) }
            }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }
    // .info/connected is a client-local path: get() would query the server and fail, so listen instead.
    private fun connected(): Flow<Boolean> = callbackFlow {
        val reference = database().getReference(".info/connected")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { trySend(snapshot.getValue(Boolean::class.java) == true) }
            override fun onCancelled(error: DatabaseError) { close(error.toException()) }
        }
        reference.addValueEventListener(listener)
        awaitClose { reference.removeEventListener(listener) }
    }
    private suspend fun requireOnline() {
        val online = withTimeoutOrNull(10_000) { connected().first { it } }
        check(online == true) { "Mất kết nối Realtime Database. Hãy kết nối mạng rồi thử lại." }
    }
    override suspend fun save(uid: String, id: String?, draft: TaskDraft) {
        require(draft.title.isNotBlank() && draft.title.length <= 200 && draft.note.length <= 4000) { "Tên tối đa 200 ký tự, ghi chú tối đa 4000 ký tự." }
        requireOnline()
        val ref = id?.let { tasks(uid).child(it) } ?: tasks(uid).push()
        // updateChildren preserves completion metadata when editing.
        withTimeout(20_000) { ref.updateChildren(TaskCodec.encode(draft)).await() }
    }
    override suspend fun toggle(uid: String, task: Task) {
        requireOnline()
        withTimeout(20_000) { tasks(uid).child(task.id).child("completedDate")
            .setValue(if (task.isDone) null else LocalDate.now().toString()).await() }
    }
    override suspend fun delete(uid: String, id: String) {
        requireOnline()
        withTimeout(20_000) { tasks(uid).child(id).removeValue().await() }
    }
}

object TaskCodec {
    fun encode(draft: TaskDraft): Map<String, Any> = mapOf(
        "title" to draft.title.trim(), "note" to draft.note.trim(), "category" to draft.category.name,
        "date" to draft.date.toString(), "time" to draft.time.toString(),
        "reminder" to draft.isReminderOn, "priority" to draft.priority.name
    )
    fun decode(id: String, data: Map<*, *>): Task = Task(
        id = id, title = data["title"] as String, note = data["note"] as? String ?: "",
        category = TaskCategory.valueOf(data["category"] as String),
        date = LocalDate.parse(data["date"] as String), time = LocalTime.parse(data["time"] as String),
        isReminderOn = data["reminder"] as? Boolean ?: false,
        priority = TaskPriority.valueOf(data["priority"] as String),
        completedDate = (data["completedDate"] as? String)?.let(LocalDate::parse)
    )
}
