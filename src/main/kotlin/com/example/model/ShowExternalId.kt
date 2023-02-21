package com.example.model

data class ShowExternalId(
    val id: Long,
    val showId: ShowID,
    val tmdbId: TMDBShowId?,
    val imdbId: String?,
    val tvdbId: Int?,
    val wikidataId: String?,
    val freebaseId: String?,
    val freebaseMid: String?,
    val tvrageId: Int?,
    val facebookId: String?,
    val instagramId: String?
)