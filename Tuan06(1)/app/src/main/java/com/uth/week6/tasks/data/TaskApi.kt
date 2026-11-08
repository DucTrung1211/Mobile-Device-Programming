package com.uth.week6.tasks.data

import com.google.gson.JsonElement
import com.uth.week6.tasks.features.todo.model.*
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.time.LocalDate
import java.time.LocalTime
import java.util.concurrent.TimeUnit

data class SubtaskDto(val title: String?, val done: Boolean?)
data class TaskDto(
    val id: String?,
    val title: String?,
    val note: String?,
    val category: String?,
    val date: String?,
    val time: String?,
    val priority: String?,
    val remind: Boolean?,
    val done: Boolean?,
    val subtasks: List<SubtaskDto>?
)
data class TaskPayload(
    val title: String,
    val note: String,
    val category: String,
    val date: String,
    val time: String,
    val priority: String,
    val remind: Boolean,
    val done: Boolean,
    val subtasks: List<SubtaskDto>
)

interface TaskApi {
    @GET("tasks") suspend fun list(): Response<List<TaskDto>>
    @GET("tasks/{id}") suspend fun get(@Path("id") id: String): Response<TaskDto>
    @POST("tasks") suspend fun create(@Body payload: TaskPayload): Response<TaskDto>
    @PATCH("tasks/{id}") suspend fun update(@Path("id") id: String, @Body payload: TaskPayload): Response<JsonElement>
    @DELETE("tasks/{id}") suspend fun delete(@Path("id") id: String): Response<JsonElement>
}

fun taskApi(token: String, baseUrl: String = "https://share.tryapps.store/mock/teach/"): TaskApi {
    val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .callTimeout(30, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .build()
    return Retrofit.Builder().baseUrl(baseUrl).client(client)
        .addConverterFactory(GsonConverterFactory.create()).build().create(TaskApi::class.java)
}

fun TaskDto.toModel(): Task {
    require(!id.isNullOrBlank() && !title.isNullOrBlank()) { "API thiếu id hoặc tên công việc." }
    val taskCategory = when (category) {
        "study" -> TaskCategory.STUDY
        "life" -> TaskCategory.LIFE
        else -> error("Nhóm công việc không hợp lệ.")
    }
    val taskPriority = when (priority) {
        "low" -> TaskPriority.LOW
        "medium" -> TaskPriority.MEDIUM
        "high" -> TaskPriority.HIGH
        else -> error("Ưu tiên không hợp lệ.")
    }
    return Task(
        id = id,
        title = title,
        note = note.orEmpty(),
        category = taskCategory,
        date = LocalDate.parse(date),
        time = LocalTime.parse(time),
        isReminderOn = requireNotNull(remind) { "API thiếu remind." },
        priority = taskPriority,
        isDone = requireNotNull(done) { "API thiếu done." },
        subtasks = subtasks.orEmpty().mapIndexed { index, item ->
            Subtask(index, requireNotNull(item.title), requireNotNull(item.done))
        }
    )
}

fun Task.payload() = TaskPayload(
    title.trim(), note, category.name.lowercase(), date.toString(),
    time.toString().take(5), priority.name.lowercase(), isReminderOn, isDone,
    subtasks.map { SubtaskDto(it.title, it.isDone) }
)
