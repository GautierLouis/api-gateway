package com.example.database.entity

import com.example.model.Episode
import com.example.model.Season
import com.example.model.Show
import com.example.model.ShowExternalId


fun ShowDAO.toModel() = Show(
    id.value,
    name,
    firstAired,
    lastAired,
    nextAired,
    poster,
    originalCountry,
    originalLanguage,
    status,
    averageRuntime,
    overview,
    numberOfSeasons,
    numberOfEpisodes,
    externalsIds.toModel(),
    seasons.map { it.toModel() },
    episodes.map { it.toModel() }
)

fun SeasonsDAO.toModel() = Season(
    id.value,
    number,
    poster,
    airDate,
    overview,
    episodes.map { it.toModel() }
)

fun ShowExternalIdsDAO.toModel() = ShowExternalId(
    id.value,
    tmdbId,
    imdbId,
    tvdbId,
    wikidataId,
    freebaseId,
    freebaseMid,
    tvrageId,
    facebookId,
    instagramId,
)

fun EpisodesDAO.toModel() = Episode(
    id.value,
    overview,
    runtime,
    stillPath,
    number,
    name,
    airDate,
    path,
    seasonNumber,
)