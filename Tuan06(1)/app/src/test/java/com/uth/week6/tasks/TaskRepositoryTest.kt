package com.uth.week6.tasks

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.uth.week6.tasks.data.*
import com.uth.week6.tasks.features.todo.model.*
import com.uth.week6.tasks.features.todo.viewmodel.TodoUiState
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TaskRepositoryTest {
    private val fixture = """{"id":7,"title":"Task","note":"Note","category":"study","date":"2026-10-20","time":"09:00","priority":"high","remind":true,"done":false,"subtasks":[{"title":"Read","done":true}]}"""
    private fun dto() = Gson().fromJson(fixture, TaskDto::class.java)

    @Test fun mapsExactJsonFieldsAndKeepsApiDate() {
        val task = dto().toModel()
        assertEquals("7", task.id)
        assertEquals(LocalDate.of(2026, 10, 20), task.date)
        assertTrue(task.isUrgent)
        assertEquals(1, task.completedSubtaskCount)
        val json = Gson().toJsonTree(task.payload()).asJsonObject
        assertTrue(json.has("remind") && json.has("done") && json.has("subtasks"))
        assertFalse(json.has("isDone"))
    }

    @Test fun statisticsUseDeviceDateAndSearchNotes() {
        val task = dto().toModel()
        val state = TodoUiState(tasks = listOf(task), today = LocalDate.of(2026, 11, 8), searchQuery = "note")
        assertTrue(state.todayTasks.isEmpty())
        assertEquals(0, state.todayProgressPercent)
        assertEquals(1, state.filteredPendingTasks.size)
        assertEquals(1, state.remainingCount(TaskCategory.STUDY))
        assertTrue(state.copy(categoryFilter = TaskCategory.LIFE).filteredPendingTasks.isEmpty())
    }

    @Test fun loadsTheSelectedIdAndRejectsWrongId() = runTest {
        val server = MockWebServer()
        server.start()
        try {
            val repo = TaskRepository(taskApi("test", server.url("/").toString()))
            server.enqueue(MockResponse().setBody(fixture))
            assertEquals("7", repo.get("7").id)
            val request = server.takeRequest()
            assertEquals("/tasks/7", request.path)
            assertEquals("Bearer test", request.getHeader("Authorization"))
            server.enqueue(MockResponse().setBody(fixture))
            try {
                repo.get("8")
                fail("Must reject mismatched id")
            } catch (_: ApiFailure) { }
        } finally { server.shutdown() }
    }

    @Test fun patchIsNotSuccessUntilDetailAndListMatch() = runTest {
        val server = MockWebServer()
        server.start()
        try {
            val repo = TaskRepository(taskApi("test", server.url("/").toString()))
            server.enqueue(MockResponse().setBody("{}"))
            server.enqueue(MockResponse().setBody(fixture))
            server.enqueue(MockResponse().setBody("[" + fixture + "]"))
            try {
                repo.save(dto().toModel().copy(isDone = true), false)
                fail("Fixed mock must not be treated as persisted")
            } catch (_: UnverifiedWrite) { }
            assertEquals("PATCH", server.takeRequest().method)
            assertEquals("GET", server.takeRequest().method)
            assertEquals("GET", server.takeRequest().method)
        } finally { server.shutdown() }
    }

    @Test fun verifiedPatchReturnsServerState() = runTest {
        val server = MockWebServer()
        server.start()
        try {
            val changed = fixture.replace("\"done\":false", "\"done\":true")
            val repo = TaskRepository(taskApi("test", server.url("/").toString()))
            server.enqueue(MockResponse().setBody("{}"))
            server.enqueue(MockResponse().setBody(changed))
            server.enqueue(MockResponse().setBody("[" + changed + "]"))
            assertTrue(repo.save(dto().toModel().copy(isDone = true), false).single().isDone)
        } finally { server.shutdown() }
    }

    @Test fun deleteRequires404AndAbsenceFromList() = runTest {
        val server = MockWebServer()
        server.start()
        try {
            val repo = TaskRepository(taskApi("test", server.url("/").toString()))
            server.enqueue(MockResponse().setBody("{}"))
            server.enqueue(MockResponse().setResponseCode(404))
            server.enqueue(MockResponse().setBody("[]"))
            assertTrue(repo.delete("7").isEmpty())
        } finally { server.shutdown() }
    }

    @Test fun httpErrorAndEmptyResponsesAreDifferent() = runTest {
        val server = MockWebServer()
        server.start()
        try {
            val repo = TaskRepository(taskApi("test", server.url("/").toString()))
            server.enqueue(MockResponse().setResponseCode(401).setBody("{}"))
            try {
                repo.list()
                fail("Must report unauthorized")
            } catch (error: ApiFailure) { assertTrue(error.message!!.contains("401")) }
            server.enqueue(MockResponse().setResponseCode(204))
            assertTrue(repo.list().isEmpty())
        } finally { server.shutdown() }
    }
}
