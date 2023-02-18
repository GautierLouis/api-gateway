package com.example.remote.tvdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ShowExtended (
    @SerialName("abbreviation") val abbreviation: String?,
    @SerialName("airsDays") val airsDays: AirsDays?,
    @SerialName("airsTime") val airsTime: String?,
    @SerialName("artworks") val artworks: List<Artwork>?,
    @SerialName("averageRuntime") val averageRuntime: Long?,
    @SerialName("characters") val characters: List<Character>?,
    @SerialName("country") val country: String?,
    @SerialName("episodes") val episodes: List<EpisodeElement>?,
    @SerialName("firstAired") val firstAired: String?,
    @SerialName("genres") val genres: List<Genre>?,
    @SerialName("id") val id: Long?,
    @SerialName("image") val image: String?,
    @SerialName("lastAired") val lastAired: String?,
    @SerialName("lastUpdated") val lastUpdated: String?,
    @SerialName("name") val name: String?,
    @SerialName("nameTranslations") val nameTranslations: List<String>?,
    @SerialName("companies") val companies: List<Network>?,
    @SerialName("nextAired") val nextAired: String?,
    @SerialName("originalCountry") val originalCountry: String?,
    @SerialName("originalLanguage") val originalLanguage: String?,
    @SerialName("originalNetwork") val originalNetwork: Network?,
    @SerialName("overview") val overview: String?,
    @SerialName("overviewTranslations") val overviewTranslations: List<String>?,
    @SerialName("remoteIDS") val remoteIDS: List<RemoteID>?,
    @SerialName("seasons") val seasons: List<Season>?,
    @SerialName("status") val status: DataStatus?,
    @SerialName("trailers") val trailers: List<Trailer>?,
)

@Serializable
data class AirsDays (
    @SerialName("friday") val friday: Boolean?,
    @SerialName("monday") val monday: Boolean?,
    @SerialName("saturday") val saturday: Boolean?,
    @SerialName("sunday") val sunday: Boolean?,
    @SerialName("thursday") val thursday: Boolean?,
    @SerialName("tuesday") val tuesday: Boolean?,
    @SerialName("wednesday") val wednesday: Boolean?
)

@Serializable
data class Alias (
    @SerialName("language") val language: String?,
    @SerialName("name") val name: String?
)

@Serializable
data class Artwork (
    @SerialName("episodeId") val episodeID: Long?,
    @SerialName("height") val height: Long?,
    @SerialName("id") val id: Long?,
    @SerialName("image") val image: String?,
    @SerialName("language") val language: String?,
    @SerialName("seasonId") val seasonID: Long?,
    @SerialName("seriesId") val seriesID: Long?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("thumbnailHeight") val thumbnailHeight: Long?,
    @SerialName("thumbnailWidth") val thumbnailWidth: Long?,
    @SerialName("updatedAt") val updatedAt: Long?,
    @SerialName("width") val width: Long?
)

@Serializable
data class Character (
    @SerialName("aliases") val aliases: List<Alias>?,
    @SerialName("episodeId") val episodeID: Long?,
    @SerialName("id") val id: Long?,
    @SerialName("image") val image: String?,
    @SerialName("isFeatured") val isFeatured: Boolean?,
    @SerialName("name") val name: String?,
    @SerialName("nameTranslations") val nameTranslations: List<String?>?,
    @SerialName("overviewTranslations") val overviewTranslations: List<String?>?,
    @SerialName("peopleId") val peopleID: Long?,
    @SerialName("personImgURL") val personImgURL: String?,
    @SerialName("peopleType") val peopleType: String?,
    @SerialName("seriesId") val seriesID: Long?,
    @SerialName("sort") val sort: Long?,
    @SerialName("url") val url: String?,
    @SerialName("personName") val personName: String?
)


@Serializable
data class Network (
    @SerialName("country") val country: String?,
    @SerialName("id") val id: Long?,
    @SerialName("inactiveDate") val inactiveDate: String?,
    @SerialName("name") val name: String?,
    @SerialName("nameTranslations") val nameTranslations: List<String?>?,
    @SerialName("overviewTranslations") val overviewTranslations: List<String?>?,
)

@Serializable
data class EpisodeElement (
    @SerialName("aired") val aired: String?,
    @SerialName("airsAfterSeason") val airsAfterSeason: Long?,
    @SerialName("airsBeforeEpisode") val airsBeforeEpisode: Long?,
    @SerialName("airsBeforeSeason") val airsBeforeSeason: Long?,
    @SerialName("finaleType") val finaleType: String?,
    @SerialName("id") val id: Long?,
    @SerialName("image") val image: String?,
    @SerialName("imageType") val imageType: Long?,
    @SerialName("lastUpdated") val lastUpdated: String?,
    @SerialName("name") val name: String?,
    @SerialName("nameTranslations") val nameTranslations: List<String>?,
    @SerialName("number") val number: Long?,
    @SerialName("overview") val overview: String?,
    @SerialName("overviewTranslations") val overviewTranslations: List<String>?,
    @SerialName("runtime") val runtime: Long?,
    @SerialName("seasonNumber") val seasonNumber: Long?,
    @SerialName("seasons") val seasons: List<Season>?,
    @SerialName("seriesId") val seriesID: Long?,
    @SerialName("seasonName") val seasonName: String?,
    @SerialName("year") val year: String?
)

@Serializable
data class Season (
    @SerialName("id") val id: Long?,
    @SerialName("image") val image: String?,
    @SerialName("imageType") val imageType: Long?,
    @SerialName("lastUpdated") val lastUpdated: String?,
    @SerialName("name") val name: String?,
    @SerialName("nameTranslations") val nameTranslations: List<String>?,
    @SerialName("number") val number: Long?,
    @SerialName("overviewTranslations") val overviewTranslations: List<String>?,
    @SerialName("seriesId") val seriesID: Long?,
    @SerialName("year") val year: String?
)

@Serializable
data class Genre (
    @SerialName("id") val id: Long?,
    @SerialName("name") val name: String?,
)

@Serializable
data class DataStatus (
    @SerialName("id") val id: Long?,
    @SerialName("keepUpdated") val keepUpdated: Boolean?,
    @SerialName("name") val name: String?,
    @SerialName("recordType") val recordType: String?
)

@Serializable
data class Trailer (
    @SerialName("id") val id: Long?,
    @SerialName("language") val language: String?,
    @SerialName("name") val name: String?,
    @SerialName("url") val url: String?,
    @SerialName("runtime") val runtime: Long?
)
