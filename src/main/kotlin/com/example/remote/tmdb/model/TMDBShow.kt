package com.example.remote.tmdb.model

import com.example.model.TMDBShowId
import com.example.sync.DataValidation
import com.example.sync.DataValidationValue
import com.example.sync.Priority
import com.example.sync.Type.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TMDBShow(
    @SerialName("backdrop_path")
    @field:DataValidation(BACKDROP, DataValidationValue.NOT_NULL, Priority.HIGH)
    val backdropPath: String?,
    @SerialName("created_by")
    @field:DataValidation(CREATOR, DataValidationValue.NOT_EMPTY_LIST)
    val createdBy: List<CreatedBy>,
    @SerialName("episode_run_time")
    @field:DataValidation(RUNTIME, DataValidationValue.NOT_EMPTY_LIST)
    val episodeRunTime: List<Long>,
    @SerialName("first_air_date")
    @field:DataValidation(FIRST_AIR_DATE, DataValidationValue.NOT_NULL)
    val firstAirDate: LocalDate,
    @SerialName("genres")
    val genres: List<TMDBGenre>,
    @SerialName("homepage")
    val homepage: String,
    @SerialName("id")
    val tmdbId: TMDBShowId,
    @SerialName("in_production")
    val inProduction: Boolean,
    @SerialName("languages")
    val languages: List<String>,
    @SerialName("last_air_date")
    val lastAirDate: LocalDate,
    @SerialName("last_episode_to_air")
    val lastEpisodeToAir: TMDBEpisode,
    @SerialName("name")
    val name: String,
    @SerialName("next_episode_to_air")
    val nextEpisodeToAir: TMDBEpisode?,
    @SerialName("networks")
    val networks: List<Network>,
    @SerialName("number_of_episodes")
    @field:DataValidation(NUMBER_OF_EPISODE, DataValidationValue.GREATER_THAN_ZERO, Priority.HIGH)
    val numberOfEpisodes: Int,
    @SerialName("number_of_seasons")
    @field:DataValidation(NUMBER_OF_SEASON, DataValidationValue.GREATER_THAN_ZERO, Priority.HIGH)
    val numberOfSeasons: Int,
    @SerialName("origin_country")
    val originCountry: List<String>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_name")
    val originalName: String,
    @SerialName("overview")
    @field:DataValidation(OVERVIEW, DataValidationValue.NOT_EMPTY_STRING, Priority.HIGH)
    val overview: String,
    @SerialName("popularity")
    val popularity: Double,
    @SerialName("poster_path")
    @field:DataValidation(POSTER, DataValidationValue.NOT_EMPTY_STRING, Priority.HIGH)
    val posterPath: String?,
    @SerialName("production_companies")
    val productionCompanies: List<Network>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountry>,
    @SerialName("seasons")
    val seasons: List<TMDBSeason>,
    @SerialName("spoken_languages")
    val spokenLanguages: List<SpokenLanguage>,
    @SerialName("status")
    val status: String,
    @SerialName("tagline")
    val tagline: String,
    @SerialName("type")
    val type: String,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Long
) {
    val realSeasons = seasons.filter { it.seasonNumber > 0 }
}

@Serializable
data class CreatedBy(
    @SerialName("id")
    val id: Long,
    @SerialName("credit_id")
    val creditID: String,
    @SerialName("name")
    val name: String,
    @SerialName("gender")
    val gender: Long,
    @SerialName("profile_path")
    val profilePath: String?
)

@Serializable
data class TMDBGenre(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String
)

@Serializable
data class Network(
    @SerialName("name")
    val name: String,
    @SerialName("id")
    val id: Long,
    @SerialName("logo_path")
    val logoPath: String?,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class ProductionCountry(
    @SerialName("iso_3166_1")
    val iso3166_1: String,
    @SerialName("name")
    val name: String
)

@Serializable
data class SpokenLanguage(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso639_1: String,
    @SerialName("name")
    val name: String
)
