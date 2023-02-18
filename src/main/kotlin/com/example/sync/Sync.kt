package com.example.sync

import com.example.local.ShowRepository
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.LastEpisodeToAir
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

        val localDbId = repository.searchShow(file.showName)
        if (localDbId == null) {
            insertNewShow(file)
        }

        // TODO Search Season then Episode

    }

    private suspend fun insertNewShow(file: VideoFile) {
        service.search(file.showName) // Search corresponding new show
            .next { service.getShow(it.id) } // Get new show (if any)
            .next { dataMapper.checkDataValidity(it) } // TODO Make sure we have all required data
            .next { repository.insertShow(it, file) } // Inset Show in database
            .next { service.getExternalId(it.tmdbId!!) } // Fetch external IDs
            .next { repository.insertExternalIds(it) } // Inset external IDs

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
    id = 0L,
    inProduction = false,
    languages = emptyList(),
    lastAirDate = mockLocalDate,
    lastEpisodeToAir = LastEpisodeToAir(
        airDate = mockLocalDate,
        episodeNumber = 0L,
        id = 0L,
        name = "",
        overview = "",
        productionCode = "",
        seasonNumber = 0L,
        stillPath = null,
        voteAverage = 0.0,
        voteCount = 0L
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