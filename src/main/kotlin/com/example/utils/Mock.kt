package com.example.utils

import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason
import com.example.remote.tmdb.model.TMDBShow
import kotlinx.datetime.LocalDate

/**
 * Should only be used to mock data (unit test, user test...)
 */

val mockLocalDate = LocalDate.fromEpochDays(0)
val mockTMDBEpisode = TMDBEpisode(
    airDate = mockLocalDate,
    episodeNumber = 1,
    id = 1,
    name = "",
    overview = "",
    productionCode = "",
    runtime = 0,
    seasonNumber = 1,
    tmdbId = 1,
    stillPath = null,
    voteAverage = 1.0,
    voteCount = 1,
    order = 1
)

val mockTMDBSeason = TMDBSeason(
    airDate = null,
    episodeCount = 1,
    id = 1L,
    name = "",
    overview = "",
    posterPath = null,
    seasonNumber = 1,
    episodes = listOf(mockTMDBEpisode)
)

val mockTMDBShow = TMDBShow(
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
    seasons = listOf(mockTMDBSeason),
    spokenLanguages = emptyList(),
    status = "",
    tagline = "",
    type = "",
    voteAverage = 0.0,
    voteCount = 0L
)