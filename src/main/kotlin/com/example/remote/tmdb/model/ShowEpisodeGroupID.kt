package com.example.remote.tmdb.model

import com.example.model.TMDBShowId
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowEpisodeGroupResponse(
    @SerialName("result") val result: List<ShowEpisodeGroupID>,
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

@Serializable
data class TMDBEpisode(
    @SerialName("air_date") val airDate: LocalDate,
    @SerialName("episode_number") val episodeNumber: Int,
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("production_code") val productionCode: String,
    @SerialName("runtime") val runtime: Int,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("show_id") val tmdbId: TMDBShowId,
    @SerialName("still_path") val stillPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("order") val order: Int?
)