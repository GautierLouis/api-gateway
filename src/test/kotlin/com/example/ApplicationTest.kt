package com.example

import com.example.database.DatabaseFactory
import com.example.database.TMDBRepository
import com.example.model.VideoFile
import com.example.plugins.configureRouting
import com.example.remote.tmdb.model.TMDBShowExternalIds
import com.example.utils.mockTMDBEpisode
import com.example.utils.mockTMDBSeason
import com.example.utils.mockTMDBShow
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

            val result = repo.insertShow(mockTMDBShow, mockExternalsIds, mockFile.showName)
                .onSuccess { entity ->
                    val newSeasons = repo.batchInsertSeasons(entity.id, listOf(mockTMDBSeason))
                    val newEpisodes = repo.batchInsertEpisodes(entity.id, newSeasons, listOf(mockTMDBEpisode), mockFile)

                    entity.episode = newEpisodes
                    entity.seasons = newSeasons
                }

            val show = repo.findShow("TEST")
            assert(result.isSuccess)
            assert(show != null)

        }
    }
}


