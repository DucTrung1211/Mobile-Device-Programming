package com.example.tuan05project.features.todo.model

import java.time.LocalDate
import java.time.LocalTime

enum class TaskCategory(val label: String) {
    STUDY("Học tập"),
    LIFE("Cuộc sống")
}

enum class TaskPriority(val label: String) {
    LOW("Thấp"),
    MEDIUM("Vừa"),
    HIGH("Cao")
}

enum class TaskRepeat(val label: String) {
    NONE("Không"),
    DAILY("Hằng ngày"),
    WEEKLY("Hằng tuần")
}

data class Subtask(
    val id: Int,
    val title: String,
    val isDone: Boolean
)

data class Task(
    val id: String,
    val title: String,
    val note: String,
    val category: TaskCategory,
    val date: LocalDate,
    val time: LocalTime,
    val isReminderOn: Boolean,
    val priority: TaskPriority,
    val repeat: TaskRepeat = TaskRepeat.NONE,
    val subtasks: List<Subtask> = emptyList(),
    val completedDate: LocalDate? = null
) {
    val isDone: Boolean
        get() = completedDate != null

    val isUrgent: Boolean
        get() = priority == TaskPriority.HIGH && !isDone

    val completedSubtaskCount: Int
        get() = subtasks.count { subtask -> subtask.isDone }
}

data class TaskDraft(
    val title: String,
    val note: String,
    val category: TaskCategory,
    val date: LocalDate,
    val time: LocalTime,
    val isReminderOn: Boolean,
    val priority: TaskPriority
)
