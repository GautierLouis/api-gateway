package com.example.sync

import com.example.database.TMDBRepository
import com.example.model.SeasonID
import com.example.model.Show
import com.example.model.TMDBShowId
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.usecases.GetCompleteShowUseCase
import com.example.usecases.SearchShowUseCase
import com.example.utils.next
import kotlinx.datetime.LocalDate


class SyncUseCase(
    private val repository: TMDBRepository,
    private val searchShowUseCase: SearchShowUseCase,
    private val getCompleteShowUseCase: GetCompleteShowUseCase,
    private val getSeasonUseCase: GetSeasonUseCase
) {
//    private val dataMapper by inject<DataMapper>()

    suspend fun execute(file: VideoFile) {
        repository.findShow(file.showName)?.let { show ->
            repository.findSeason(show.id, file.seasonNumber)?.let { season ->
                repository.findEpisode(season.id, file.seasonNumber)?.let { episodeId ->
                    // TODO What do we do ? Update file path ? Show duplicate ? Nothing
                } ?: insertNewEpisode(season.id, file)
            } ?: insertNewSeason(show, file)
        } ?: insertNewShow(file)
    }


    private suspend fun insertNewShow(videoFile: VideoFile) {
        searchShowUseCase.execute(videoFile.showName)
//            .next { /* TODO Check Data validity */ }
            .next { getCompleteShowUseCase.execute(it.id, videoFile) }
    }

    private suspend fun insertNewSeason(show: Show, videoFile: VideoFile) {
//        getSeasonUseCase.execute(show.externalId!!.tmdbId, videoFile.seasonNumber)
    }

    private suspend fun insertNewEpisode(seasonID: SeasonID, videoFile: VideoFile) {

    }
}

class GetSeasonUseCase(
    private val service: TMDBService,
    private val repository: TMDBRepository,
) {
    suspend fun execute(id: TMDBShowId, seasonNumber: Int) {
        service.getSeason(id, seasonNumber)
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