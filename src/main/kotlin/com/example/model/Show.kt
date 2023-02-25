package com.example.model

import kotlinx.datetime.LocalDate

typealias ShowID = Long
typealias SeasonID = Long
typealias EpisodeID = Long
typealias TMDBShowId = Long
typealias TMDBSeasonId = Long
typealias TMDBEpisodeId = Long

data class Show(
    val id: ShowID,
    val name: String,
    val firstAired: LocalDate,
    val lastAired: LocalDate,
    val nextAired: LocalDate?,
    val poster: String,
    val originalCountry: String,
    val originalLanguage: String,
    val status: String,
    val averageRuntime: Double,
    val overview: String,
    val numberOfSeasons: Int,
    val numberOfEpisodes: Int,
    val externalId: ShowExternalId,
    val seasons: List<Season>,
    val episode: List<Episode>,
)
