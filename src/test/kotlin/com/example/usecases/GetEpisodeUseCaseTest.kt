package com.example.usecases

import com.example.database.DatabaseFactory
import com.example.database.TMDBRepository
import com.example.model.VideoFile
import com.example.plugins.SecretsEnv.TMDB_ENGINE
import com.example.remote.ClientEngine
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.sync.GetEpisodeUseCase
import com.example.utils.mockExternalIds
import com.example.utils.mockFile
import com.example.utils.mockTMDBEpisode
import com.example.utils.mockTMDBShow
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.test.BeforeTest


class GetEpisodeUseCaseTest {

    private val mockEngine = MockEngine { request ->
        respond(
            content = ByteReadChannel(Json.encodeToString(TMDBEpisode.serializer(), mockTMDBEpisode)),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    @BeforeTest
    fun prepare() {
        DatabaseFactory.initInFile()
        startKoin {
            modules(module {
                single(named(TMDB_ENGINE)) {
                    ClientEngine().intTest(
                        Url("http://mock.url"),
                        mockEngine
                    )
                }
            })
        }

        // Add Show and Season in database
        val repo = TMDBRepository()
        runBlocking {
            repo.insertShow(
                mockTMDBShow,
                mockExternalIds,
                "Clean Name"
            )

        }
    }

    @Test
    fun insertNewEpisodeInDb() {
        val service = TMDBService("token")
        val repository = TMDBRepository()

        val useCase = GetEpisodeUseCase(service, repository)

        runBlocking {
//            val result = service.getEpisode(0L, 0, 0)
//            assert(result.isSuccess)
//            assertEquals(result.getOrThrow(), mockTMDBEpisode)


            useCase.execute(
                1L,
                1L,
                mockTMDBShow.tmdbId,
                VideoFile("Clean Name", 0, 0, mockFile)
            )
        }

    }
}