package com.example.sync

import com.example.database.ShowRepository
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.utils.next
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class Sync : KoinComponent {

    private val service by inject<TMDBService>()
    private val repository by inject<ShowRepository>()
    private val dataMapper by inject<DataMapper>()

    suspend fun triggerSync(file: VideoFile) {

        val localDbId = repository.findShow(file.showName)
        if (localDbId == null) {
            insertNewShow(file)
        }

        // TODO Search Season then Episode

    }

    private suspend fun insertNewShow(videoFile: VideoFile) {
        service.searchSingle(videoFile.showName) // Search corresponding new show
            .next { service.getShowExternalId(it.id) } // Fetch external IDs
            .next { repository.insertExternalIds(it) } // Inset external IDs
            .next { service.getShow(it.tmdbId!!) } // Get new show (if any)
//            .next { dataMapper.checkDataValidity(it) } // TODO Make sure we have all required data
            .next { repository.insertShow(it, videoFile) } // Inset Show in database
            .next { service.getAllSeasonsAndEpisodes(it) }
            .next { repository.insertEpisodes(it, videoFile.file) }

    }
}


val mockLocalDate = LocalDate.fromEpochDays(0)
val mock = TMDBShow(
    backdropPath = "",
    createdBy = emptyList(),
    episodeRunTime = listOf(55),
    firstAirDate = mockLocalDate,
    genres = emptyList(),
    homepage = "",
    tmdbId = 0L,
    inProduction = false,
    languages = emptyList(),
    lastAirDate = mockLocalDate,
    lastEpisodeToAir = TMDBEpisode(
        airDate = mockLocalDate,
        episodeNumber = 0,
        id = 0L,
        name = "",
        overview = "",
        productionCode = "",
        seasonNumber = 0,
        stillPath = null,
        voteAverage = 0.0,
        voteCount = 0,
        runtime = 0,
        order = 0,
        tmdbId = 0L
    ),
    name = "",
    nextEpisodeToAir = null,
    networks = emptyList(),
    numberOfEpisodes = 0L,
    numberOfSeasons = 0L,
    originCountry = emptyList(),
    originalLanguage = "",
    originalName = "",
    overview = "",
    popularity = 0.0,
    posterPath = null,
    productionCompanies = emptyList(),
    productionCountries = emptyList(),
    seasons = emptyList(),
    spokenLanguages = emptyList(),
    status = "In Production",
    tagline = "",
    type = "",
    voteAverage = 0.0,
    voteCount = 0L
)