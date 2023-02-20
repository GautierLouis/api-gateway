package com.example.model

import kotlinx.datetime.LocalDate

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