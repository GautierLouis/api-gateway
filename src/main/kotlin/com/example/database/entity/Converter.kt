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
)

fun SeasonsDAO.toModel() = Season(
    id.value,
    number,
    poster,
    airDate,
    overview
)

fun ShowExternalIdsDAO.toModel() = ShowExternalId(
    id.value,
    show.id.value,
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