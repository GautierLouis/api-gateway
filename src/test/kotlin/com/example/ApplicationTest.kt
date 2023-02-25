package com.example

import com.example.database.DatabaseFactory
import com.example.database.TMDBRepository
import com.example.model.VideoFile
import com.example.plugins.configureRouting
import com.example.remote.tmdb.model.TMDBShowExternalIds
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    private lateinit var database: Database


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

    @BeforeTest
    fun prepare() {
        database = DatabaseFactory.initInFile()
    }

    @AfterTest
    fun purge() {
        transaction {
            SchemaUtils.dropDatabase(database.url)
        }
    }


    @Test
    fun testDatabase() = testApplication {

        val repo = TMDBRepository()

        runBlocking {
            val mockFile = VideoFile("TEST", 1, 1, File.createTempFile("prefix", "suffix"))
            val mockExternalsIds = TMDBShowExternalIds(1, null, "", "", 1, 1, "", "", "")

            val show = repo.findShow("TEST")
            assert(show != null)

        }
    }
}


