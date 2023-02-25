package com.example.model

import kotlinx.datetime.LocalDate

data class Season(
    val id: SeasonID,
    val number: Int,
    val poster: String,
    val airDate: LocalDate,
    val overview: String,
    val episodes: List<Episode>
)