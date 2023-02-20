package com.example.remote.tmdb.model

import com.example.model.TMDBSeasonId
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TMDBSeason(
    @SerialName("air_date")
    val airDate: LocalDate?,
    @SerialName("episode_count")
    val episodeCount: Long,
    @SerialName("id")
    val id: TMDBSeasonId,
    @SerialName("name")
    val name: String,
    @SerialName("overview")
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("season_number")
    val seasonNumber: Int,
    @SerialName("episodes")
    val episodes: List<TMDBEpisode>
)