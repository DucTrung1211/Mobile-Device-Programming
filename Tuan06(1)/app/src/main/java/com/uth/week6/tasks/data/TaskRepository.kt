package com.uth.week6.tasks.data

import com.uth.week6.tasks.features.todo.model.Task
import retrofit2.Response
import java.io.IOException

class ApiFailure(message: String) : IOException(message)
class UnverifiedWrite : IOException("Máy chủ đã nhận yêu cầu nhưng dữ liệu đọc lại không khớp. API có thể trả mock cố định; thay đổi CHƯA được xác nhận lưu.")

fun <T> Response<T>.checked(): T {
    checkStatus()
    return body() ?: throw ApiFailure("HTTP " + code() + ": phản hồi trống.")
}
fun Response<*>.checkStatus() {
    if (!isSuccessful) {
        errorBody()?.close()
        throw ApiFailure(when (code()) {
            401, 403 -> "HTTP " + code() + ": token không hợp lệ hoặc không đủ quyền."
            429 -> "HTTP 429: quá nhiều yêu cầu. Vui lòng thử lại sau."
            else -> "HTTP " + code() + ": máy chủ không xử lý được yêu cầu."
        })
    }
}

class TaskRepository(private val api: TaskApi) {
    suspend fun list(): List<Task> {
        val response = api.list()
        response.checkStatus()
        return response.body().orEmpty().map { it.toModel() }
    }

    suspend fun get(id: String): Task {
        val task = api.get(id).checked().toModel()
        if (task.id != id) throw ApiFailure("API trả công việc khác id đã chọn.")
        return task
    }

    suspend fun save(task: Task, creating: Boolean): List<Task> {
        val savedId = if (creating) {
            api.create(task.payload()).checked().id
                ?: throw ApiFailure("POST không trả id để kiểm chứng.")
        } else {
            api.update(task.id, task.payload()).checkStatus()
            task.id
        }
        // Không dùng phản hồi PATCH làm bằng chứng máy chủ đã lưu.
        val detail = get(savedId)
        val all = list()
        if (detail.payload() != task.payload() || all.none { it.id == savedId && it.payload() == task.payload() }) {
            throw UnverifiedWrite()
        }
        return all
    }

    suspend fun delete(id: String): List<Task> {
        api.delete(id).checkStatus()
        val detail = api.get(id)
        val absent = detail.code() == 404
        if (!absent) detail.checkStatus()
        val all = list()
        if (!absent || all.any { it.id == id }) throw UnverifiedWrite()
        return all
    }
}
