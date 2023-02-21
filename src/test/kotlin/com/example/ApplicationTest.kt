package com.example

import com.example.database.DatabaseFactory
import com.example.database.TMDBRepository
import com.example.model.VideoFile
import com.example.plugins.configureRouting
import com.example.sync.mock
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @AfterTest
    fun purge() {
        val db = File("/Users/louisgautier/Desktop/test/data.db")
//        db.delete()
    }


    @Test
    fun testDatabase() = testApplication {
        DatabaseFactory.initInFile()
        val repo = TMDBRepository()

        runBlocking {
            val result = repo.insertShow(mock, VideoFile("TEST", 1, 1, File.createTempFile("prefix", "suffix")))
            val show = repo.findShow("TEST")
            assert(result.isSuccess)
            assert(show != null)
        }
    }
}


