package com.example.model

data class ShowExternalId(
    val dbId: Int,
    val showId: ShowID,
    val tmdbId: Long?,
    val imdbId: String?,
    val tvdbId: Int?,
    val wikidataId: String?,
    val freebaseId: String?,
    val freebaseMid: String?,
    val tvrageId: Int?,
    val facebookId: String?,
    val instagramId: String?
)