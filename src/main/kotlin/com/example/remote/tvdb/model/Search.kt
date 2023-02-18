package com.example.remote.tvdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TVDBSearch(
//    @SerialName("objectID") val objectID: String,
//    @SerialName("aliases") val aliases: List<String>? = emptyList(),
//    @SerialName("country") val country: String,
    @SerialName("id") val id: String,
//    @SerialName("image_url") val imageURL: String,
    @SerialName("name") val name: String,
//    @SerialName("first_air_time") val firstAirTime: String,
//    @SerialName("overview") val overview: String,
//    @SerialName("primary_language") val primaryLanguage: String,
//    @SerialName("primary_type") val primaryType: String,
//    @SerialName("status") val status: String,
//    @SerialName("type") val type: String,
    @SerialName("tvdb_id") val tvdbID: String,
//    @SerialName("year") val year: String,
//    @SerialName("slug") val slug: String,
//    @SerialName("overviews") val overviews: Map<String, String>,
//    @SerialName("translations") val translations: Map<String, String>,
//    @SerialName("network") val network: String? = null,
    @SerialName("remote_ids") val remoteIDS: List<RemoteID> = emptyList(),
//    @SerialName("thumbnail") val thumbnail: String
)

@Serializable
data class RemoteID(
    @SerialName("id") val id: String,
    @SerialName("type") val type: Long,
    @SerialName("sourceName") val sourceName: String
)