package com.example.remote.tmdb.model

import com.example.model.TMDBEpisodeId
import com.example.model.TMDBShowId
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TMDBEpisode(
    @SerialName("air_date") val airDate: LocalDate?,
    @SerialName("episode_number") val episodeNumber: Int,
    @SerialName("id") val id: TMDBEpisodeId,
    @SerialName("name") val name: String,
    @SerialName("overview") val overview: String,
    @SerialName("production_code") val productionCode: String,
    @SerialName("runtime") val runtime: Int? = 0,
    @SerialName("season_number") val seasonNumber: Int,
    @SerialName("show_id") val tmdbId: TMDBShowId,
    @SerialName("still_path") val stillPath: String?,
    @SerialName("vote_average") val voteAverage: Double,
    @SerialName("vote_count") val voteCount: Int,
    @SerialName("order") val order: Int?
)