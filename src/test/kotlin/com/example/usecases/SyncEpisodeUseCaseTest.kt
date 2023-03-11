package com.example.usecases

import com.example.database.DatabaseFactory
import com.example.file.FileInfo
import com.example.file.VideoFileExport
import com.example.model.Show
import com.example.model.ShowExternalId
import com.example.plugins.SecretsEnv.TMDB_ENGINE
import com.example.plugins.SecretsEnv.TMDB_TOKEN
import com.example.plugins.koinModules
import com.example.remote.ClientEngine
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.utils.mockLocalDate
import com.example.utils.mockTMDBEpisode
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest


class SyncEpisodeUseCaseTest : KoinComponent {

    private val showId = 1L
    private val seasonNumber = 2
    private val episodeNumber = 3
    private val episodeId = 4L
    private val mockResponse = mockTMDBEpisode.copy(
        tmdbId = showId,
        seasonNumber = seasonNumber,
        episodeNumber = episodeNumber,
        id = episodeId
    )

    private val mockShow = Show(
        id = 1L,
        name = "",
        firstAired = mockLocalDate,
        lastAired = mockLocalDate,
        nextAired = null,
        poster = "",
        originalCountry = "",
        originalLanguage = "",
        status = "",
        averageRuntime = 0.0,
        numberOfEpisodes = 1,
        numberOfSeasons = 1,
        episode = emptyList(),
        overview = "",
        seasons = emptyList(),
        externalId = ShowExternalId(
            id = 1L,
            tmdbId = showId,
            imdbId = null,
            tvdbId = null,
            wikidataId = null,
            freebaseId = null,
            freebaseMid = null,
            tvrageId = null,
            facebookId = null,
            instagramId = null
        )
    )

    private val mockEngine = MockEngine { _ ->
        respond(
            content = ByteReadChannel(Json.encodeToString(TMDBEpisode.serializer(), mockResponse)),
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    }

    private lateinit var database: Database

    @BeforeTest
    fun prepare() {
        database = DatabaseFactory.initInFile()
        startKoin {
            modules(
                module {
                    // Mock module
                    single(named(TMDB_TOKEN)) { "MOCK_TOKEN" }
                    single(named(TMDB_ENGINE)) {
                        ClientEngine().intTest(
                            Url("http://mock.url"),
                            mockEngine
                        )
                    }
                }
            )
            modules(koinModules)
        }
    }

    @AfterTest
    fun after() {
        transaction {
//            SchemaUtils.dropDatabase(database.url)
        }
    }

    @Test
    fun insertNewEpisode() {
        val useCase = SyncEpisodeUseCase(get(), get())

        runBlocking {
            val result = useCase.execute(
                mockShow,
                1L,
                VideoFileExport("/tmp/video.mkv", "", "", FileInfo.Default(seasonNumber, episodeNumber))
            )

            assert(result.isSuccess)

        }
    }
}