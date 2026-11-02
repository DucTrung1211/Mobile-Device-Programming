package com.example.tuan05project

import com.example.tuan05project.auth.AuthUser
import com.example.tuan05project.data.*
import com.example.tuan05project.features.todo.model.*
import com.example.tuan05project.features.todo.viewmodel.*
import com.example.tuan05project.navigation.*
import com.example.tuan05project.messaging.validTaskId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class TaskStateTest {
    @get:Rule val main = MainDispatcherRule()
    private val draft = TaskDraft("Việc thật", "Ghi chú", TaskCategory.STUDY, LocalDate.of(2026,11,2), LocalTime.of(9,0), false, TaskPriority.HIGH)
    private fun task(id: String, done: Boolean = false) = TaskCodec.decode(id, TaskCodec.encode(draft) +
        if (done) mapOf("completedDate" to draft.date.toString()) else emptyMap())
    @Test fun databaseCodecRoundTripsCompletionAndDraft() {
        val decoded = task("-firebasePushId", true)
        assertEquals(draft.title, decoded.title); assertEquals(draft.time, decoded.time)
        assertTrue(decoded.isDone); assertEquals(draft.date, decoded.completedDate)
        assertFalse(TaskCodec.encode(draft).containsKey("completedDate"))
    }
    @Test fun statsAndFiltersUseOnlyRealTasks() {
        val state = TodoUiState(tasks = listOf(task("a", true), task("b")), today = draft.date,
            categoryFilter = TaskCategory.STUDY, searchQuery = "thật")
        assertEquals(1, state.todayDoneCount); assertEquals(50, state.todayProgressPercent)
        assertEquals(1, state.filteredPendingTasks.size); assertEquals(1, state.filteredDoneTasks.size)
        assertEquals(0, percent(0,0))
    }
    @Test fun logoutGateNeverReturnsProtectedRoutesAndGuestIsExplicit() {
        for (route in listOf("home","profile","services")) assertEquals(Destination.LOGIN, destinationFor(2,false,false,route))
        assertEquals(Destination.PROFILE, destinationFor(1,true,false,"profile"))
        assertEquals(Destination.HOME, destinationFor(2,false,true,"services"))
        assertEquals(Destination.LEGACY, destinationFor(3,false,false,"home"))
        assertNull(validTaskId("../alice/tasks")); assertNull(validTaskId("https://untrusted.test"))
        assertEquals("-Push_123", validTaskId("-Push_123"))
    }
    @Test fun missingUserFieldsHaveSafeFallbacks() {
        val user = AuthUser("uid", null, null, null, emptyList())
        assertEquals("Bạn", user.displayName)
        assertFalse(user.providerLabel.contains("Google"))
    }
    private class FakeTasks : TaskRepository {
        val feed = MutableStateFlow<List<Task>>(emptyList())
        var saves = 0
        var failSave = false
        override fun observe(uid: String) = feed
        override suspend fun save(uid: String, id: String?, draft: TaskDraft) { saves++; if (failSave) error("Permission denied") }
        override suspend fun toggle(uid: String, task: Task) {}
        override suspend fun delete(uid: String, id: String) {}
    }
    @Test fun saveFailureKeepsEditorAndSessionSwitchClearsPreviousUsersTasks() = runTest {
        val repo = FakeTasks()
        val vm = TodoViewModel(repo, enableServices = false)
        val alice = AuthUser("alice", "Alice", null, null, listOf("google.com"))
        vm.bind(alice, true); runCurrent()
        repo.feed.value = listOf(task("alice-task")); runCurrent()
        vm.openEditor(); repo.failSave = true
        vm.saveTask(null, draft); runCurrent()
        assertTrue(vm.uiState.value.editorOpen); assertFalse(vm.uiState.value.busy)
        assertTrue(vm.uiState.value.message!!.contains("quyền"))
        vm.bind(alice.copy(uid = "bob"), true)
        assertTrue(vm.uiState.value.tasks.isEmpty())
        assertFalse(vm.uiState.value.editorOpen)
        vm.bind(null, false); runCurrent()
        assertTrue(vm.uiState.value.tasks.isEmpty())
        vm.saveTask(null, draft); runCurrent()
        assertEquals(1, repo.saves)
    }
}
