package com.example.remote.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TMDBShowExternalIds(
    @SerialName("id") val id: Long,
    @SerialName("imdb_id") val imdbId: String?,
    @SerialName("freebase_mid") val freebaseMid: String?,
    @SerialName("freebase_id") val freebaseId: String?,
    @SerialName("tvdb_id") val tvdbId: Int?,
    @SerialName("tvrage_id") val tvrageId: Int?,
    @SerialName("wikidata_id") val wikidataId: String?,
    @SerialName("facebook_id") val facebookId: String?,
    @SerialName("instagram_id") val instagramId: String?
)