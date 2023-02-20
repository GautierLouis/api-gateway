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
    val numberOfEpisodes: Int
)


data class Episode(
    val id: EpisodeID,
    val overview: String,
    val runtime: Int,
    val thumbnail: String,
    val number: Int,
    val name: String,
    val aired: LocalDate,
    val path: String,
    val seasonNumber: Int,
)

data class Season(
    val id: SeasonID,
    val number: Int,
    val poster: String,
    val airDate: LocalDate,
    val overview: String
)