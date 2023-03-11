package com.example.utils

import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import kotlinx.datetime.LocalDate
import java.io.File

/**
 * Should only be used to mock data (unit test, user test...)
 */
val mockFile =
    File("/Users/louisgautier/Documents/Plex/The.Boys.S03.MULTi.1080p.AMZN.WEBRip.DDP5.1.x265-R3MiX/The.Boys.S03E01.MULTi.1080p.AMZN.WEBRip.DDP5.1.x265-R3MiX.mkv")
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

val mockExternalIds = TMDBShowExternalIds(
    imdbId = null,
    freebaseMid = null,
    freebaseId = null,
    tvdbId = null,
    tvrageId = null,
    wikidataId = null,
    facebookId = null,
    instagramId = null,
    tmdbId = 0L
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
    voteCount = 0L,
    mockExternalIds
)