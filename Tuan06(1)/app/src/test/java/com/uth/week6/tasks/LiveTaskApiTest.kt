package com.uth.week6.tasks

import com.uth.week6.tasks.data.*
import kotlinx.coroutines.runBlocking
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.util.UUID

class LiveTaskApiTest {
    @Test fun verifyRealEndpointsWithoutLoggingCredentials() = runBlocking {
        assumeTrue(System.getProperty("liveApi") == "true")
        val token = System.getProperty("taskToken")
        require(!token.isNullOrBlank())
        val api = taskApi(token)
        val response = api.list()
        println("GET tasks: HTTP " + response.code())
        val tasks = response.checked().map { it.toModel() }
        require(tasks.isNotEmpty())
        for (task in tasks) {
            val detail = api.get(task.id)
            println("GET tasks/" + task.id + ": HTTP " + detail.code() + ", idMatches=" + (detail.body()?.id == task.id))
            detail.checkStatus()
        }
        val original = tasks.first { it.id == "1" || it.id == tasks.first().id }
        val changed = original.copy(isDone = !original.isDone)
        val patch = api.update(original.id, changed.payload())
        patch.checkStatus()
        val reread = api.get(original.id).checked().toModel()
        val patchPersisted = reread.payload() == changed.payload()
        println("PATCH tasks/" + original.id + ": HTTP " + patch.code() + ", persisted=" + patchPersisted)
        if (patchPersisted) {
            api.update(original.id, original.payload()).checkStatus()
            require(api.get(original.id).checked().toModel().payload() == original.payload())
        }
        val created = original.copy(id = "", title = "UTH API verification " + UUID.randomUUID())
        val post = api.create(created.payload())
        post.checkStatus()
        val id = post.body()?.id
        val after = api.list().checked().map { it.toModel() }
        val persisted = id != null && after.any { it.id == id && it.title == created.title }
        println("POST tasks: HTTP " + post.code() + ", persisted=" + persisted)
        if (persisted && id !in tasks.map { it.id }) {
            val deletion = api.delete(id)
            deletion.checkStatus()
            println("DELETE created task: HTTP " + deletion.code() + ", absent=" + api.list().checked().none { it.id == id })
        } else {
            val sentinel = "999999999"
            val deletion = api.delete(sentinel)
            println("DELETE nonexistent sentinel: HTTP " + deletion.code())
            require(deletion.isSuccessful || deletion.code() == 404)
        }
    }
}
