package com.example.remote.tmdb.model

import com.example.model.TMDBShowId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowEpisodeGroupResponse(
    @SerialName("results") val result: List<ShowEpisodeGroupID>,
    @SerialName("id") val TMDBShowId: TMDBShowId
)

@Serializable
data class ShowEpisodeGroupID(
    @SerialName("description") val description: String,
    @SerialName("episode_count") val episodeCount: Int,
    @SerialName("group_count") val groupCount: Int,
    @SerialName("id") val groupId: String,
    @SerialName("name") val name: String,
    @SerialName("type") val type: Int,
    @SerialName("groups") val groups: List<EpisodeGroup>?
)

@Serializable
data class EpisodeGroup(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("order") val order: Int,
    @SerialName("locked") val locked: Boolean,
    @SerialName("episodes") val episodes: List<TMDBEpisode>
)