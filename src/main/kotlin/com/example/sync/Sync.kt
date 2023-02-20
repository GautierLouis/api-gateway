package com.example.sync

import com.example.database.TMDBRepository
import com.example.model.VideoFile
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.usecases.GetCompleteShowUseCase
import com.example.usecases.SearchShowUseCase
import com.example.utils.next
import kotlinx.datetime.LocalDate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class Sync : KoinComponent {

    private val repository by inject<TMDBRepository>()
    private val dataMapper by inject<DataMapper>()

    private val searchShowUseCase by inject<SearchShowUseCase>()
    private val getShowUseCase by inject<GetCompleteShowUseCase>()

    suspend fun triggerSync(file: VideoFile) {

        val localDbId = repository.findShow(file.showName)
        if (localDbId == null) {
            insertNewShow(file)
        }

        // TODO Search Season then Episode

    }

    private suspend fun insertNewShow(videoFile: VideoFile) {
        searchShowUseCase.execute(videoFile.showName)
            .next { getShowUseCase.execute(it.id, videoFile) }
    }
}

val mockLocalDate = LocalDate.fromEpochDays(0)
val mock = TMDBShow(
    backdropPath = "",
    createdBy = emptyList(),
    episodeRunTime = listOf(0),
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
        productionCode = "101",
        seasonNumber = 1,
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
    numberOfEpisodes = 0,
    numberOfSeasons = 0,
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
    status = "",
    tagline = "",
    type = "",
    voteAverage = 0.0,
    voteCount = 0L
)