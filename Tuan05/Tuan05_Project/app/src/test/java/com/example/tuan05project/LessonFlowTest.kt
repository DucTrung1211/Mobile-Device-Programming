package com.example.tuan05project

import com.example.tuan05project.auth.*
import com.example.tuan05project.data.*
import com.example.tuan05project.features.todo.model.*
import com.example.tuan05project.features.todo.viewmodel.*
import com.example.tuan05project.navigation.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalCoroutinesApi::class)
class LessonFlowTest {
    @get:Rule val main = MainDispatcherRule()
    private val alice = AuthUser("alice", "Alice Nguyen", "alice@example.test", null, listOf("google.com"))
    private val draft = TaskDraft("Ôn chương 3", "", TaskCategory.STUDY, LocalDate.of(2026, 11, 3), LocalTime.of(14, 0), false, TaskPriority.MEDIUM)

    private class NeverEmittingAuth : AuthRepository {
        val emitter = MutableSharedFlow<AuthUser?>()
        override val users: Flow<AuthUser?> get() = emitter
        override suspend fun signInWithGoogle(idToken: String) = error("not used")
        override suspend fun rename(name: String) = Unit
        override fun signOut() = Unit
    }

    private class RecordingTelemetry : Telemetry {
        val events = mutableListOf<Triple<String, String?, String?>>()
        override fun event(name: String, parameter: String?, value: String?) { events += Triple(name, parameter, value) }
    }

    private class FakeTasks : TaskRepository {
        val feed = MutableStateFlow<List<Task>>(emptyList())
        var failObserve = false
        val calls = mutableListOf<String>()
        override fun observe(uid: String): Flow<List<Task>> =
            if (failObserve) flow { throw IllegalStateException("Permission denied") } else feed
        override suspend fun save(uid: String, id: String?, draft: TaskDraft) {
            calls += "save:$uid:${id ?: "new"}"
            feed.value = feed.value + TaskCodec.decode(id ?: "p${feed.value.size}", TaskCodec.encode(draft))
        }
        override suspend fun toggle(uid: String, task: Task) {
            calls += "toggle:$uid:${task.id}"
            feed.value = feed.value.map { if (it.id == task.id) it.copy(completedDate = if (it.isDone) null else it.date) else it }
        }
        override suspend fun delete(uid: String, id: String) {
            calls += "delete:$uid:$id"
            feed.value = feed.value.filterNot { it.id == id }
        }
    }

    @Test fun restoringSessionNeverShowsLoginButPickerStaysReachable() {
        assertEquals(Destination.RESTORING, destinationFor(Lesson.AUTH, true, false, false, "home"))
        assertEquals(Destination.RESTORING, destinationFor(Lesson.FIREBASE, true, false, false, "profile"))
        assertEquals(Destination.LESSONS, destinationFor(Lesson.PICKER, true, false, false, "home"))
        assertEquals(Destination.LOGIN, destinationFor(Lesson.AUTH, false, false, false, "profile"))
    }

    @Test fun authStateStaysRestoringUntilFirebaseReportsSessionAndLogoutDoesNotReturnToRestoring() = runTest {
        val repo = NeverEmittingAuth()
        val vm = AuthViewModel(repo); runCurrent()
        assertTrue(vm.uiState.value.restoring)
        repo.emitter.emit(alice); runCurrent()
        assertFalse(vm.uiState.value.restoring); assertEquals(alice, vm.uiState.value.user)
        vm.signOut(clearCredential = {}); runCurrent()
        assertFalse(vm.uiState.value.restoring); assertNull(vm.uiState.value.user)
    }

    @Test fun cleanupTimeoutStillSignsOutInsteadOfLeavingLogoutStuck() = runTest {
        var signOuts = 0
        val repo = object : AuthRepository {
            val state = MutableStateFlow<AuthUser?>(alice)
            override val users: Flow<AuthUser?> get() = state
            override suspend fun signInWithGoogle(idToken: String) = error("not used")
            override suspend fun rename(name: String) = Unit
            override fun signOut() { signOuts++; state.value = null }
        }
        val vm = AuthViewModel(repo); runCurrent()
        var cleared = false
        vm.signOut(clearCredential = { cleared = true },
            beforeSignOut = { withTimeout(8_000) { delay(60_000) } })
        advanceUntilIdle()
        assertEquals(1, signOuts); assertTrue(cleared)
        assertNull(vm.uiState.value.user); assertFalse(vm.uiState.value.loading)
        assertTrue(vm.uiState.value.message!!.contains("Chưa xác nhận dọn"))
    }

    @Test fun lessonOneSessionCannotWriteTasks() = runTest {
        val repo = FakeTasks()
        val vm = TodoViewModel(repo, enableServices = false)
        vm.bind(alice, integrated = false); runCurrent()
        vm.openEditor(); vm.saveTask(null, draft); runCurrent()
        assertFalse(vm.uiState.value.editorOpen)
        assertTrue(repo.calls.isEmpty())
        assertTrue(vm.uiState.value.message!!.contains("Bài 02"))
    }

    @Test fun crudUpdatesStateProgressAndLogsOnlyNonPersonalEventsAfterSuccess() = runTest {
        val repo = FakeTasks(); val telemetry = RecordingTelemetry()
        val vm = TodoViewModel(repo, telemetry, enableServices = false)
        vm.bind(alice, integrated = true); runCurrent()
        vm.openEditor(); vm.saveTask(null, draft); runCurrent()
        assertEquals(listOf("save:alice:new"), repo.calls)
        assertFalse(vm.uiState.value.editorOpen)
        val id = vm.uiState.value.tasks.single().id
        val state = vm.uiState.value.copy(today = draft.date)
        assertEquals(0, state.todayProgressPercent); assertEquals(1, state.remainingCount(TaskCategory.STUDY))

        vm.toggleTaskDone(id); runCurrent()
        assertTrue(vm.uiState.value.findTask(id)!!.isDone)
        assertEquals(100, vm.uiState.value.copy(today = draft.date).todayProgressPercent)

        vm.deleteTask(id); runCurrent()
        assertTrue(vm.uiState.value.tasks.isEmpty())
        assertEquals(listOf("save:alice:new", "toggle:alice:$id", "delete:alice:$id"), repo.calls)
        assertEquals(listOf("task_create", "task_complete"), telemetry.events.map { it.first })
        val sent = telemetry.events.flatMap { listOfNotNull(it.second, it.third) }.joinToString()
        for (secret in listOf(alice.uid, alice.email!!, alice.name!!, draft.title)) assertFalse(sent.contains(secret, true))
    }

    @Test fun listenerErrorShowsMessageAndRetryRecovers() = runTest {
        val repo = FakeTasks().apply { failObserve = true }
        val vm = TodoViewModel(repo, enableServices = false)
        vm.bind(alice, integrated = true); runCurrent()
        assertFalse(vm.uiState.value.loading)
        assertTrue(vm.uiState.value.error!!.contains("từ chối quyền"))
        repo.failObserve = false
        repo.feed.value = listOf(TaskCodec.decode("t1", TaskCodec.encode(draft)))
        vm.retry(); runCurrent()
        assertNull(vm.uiState.value.error); assertEquals(1, vm.uiState.value.tasks.size)
    }

    @Test fun dataErrorsAreSpecific() {
        assertTrue(dataError(RuntimeException("Permission denied")).contains("quyền"))
        assertTrue(dataError(IllegalStateException("Chưa cấu hình Realtime Database.")).contains("Realtime Database"))
        assertTrue(dataError(RuntimeException("socket")).contains("mạng"))
    }
}
