package com.example.model

import kotlinx.datetime.LocalDate

typealias ShowID = Int

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
)


data class Episode(
    val id: Int,
    val seriesId: Int,
    val overview: String,
    val runTime: Int,
    val image: String,
    val number: Int,
    val seasonNumber: Int,
    val name: String,
    val aired: LocalDate,
    val path: String
)

data class Characters(
    val id: String,
    val name: String,
    val peopleId: Int,
    val showId: Int,
    val image: String,
    val url: String,
    val personName: String,
    val personImgURL: String,
    val sort: Int,
)

enum class ShowSourceName(code: String) {
    IMDB("IMDB"),
    TMDB("TheMovieDB.com"),
    EIDR("EIDR"),
    TVMAZE("TV Maze"),
    OTHER("other"),
    TVDB("")
}

data class Season(
    val id: Long,
    val showId: Long,
    val number: Int,
    val poster: String,
)

data class Genre(
    val id: String,
    val name: String
)

data class Tag(
    val id: String,
    val name: String,
)