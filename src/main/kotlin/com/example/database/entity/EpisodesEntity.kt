package com.example.database.entity

import com.example.model.SeasonID
import com.example.model.ShowID
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.utils.mockLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object EpisodesEntity : LongIdTable("episode") {
    val number = integer("number")
    val path = text("path").nullable()
    val airDate = date("air_date")
    val name = varchar128("name")
    val overview = text("overview")
    val productionCode = varchar128("production_code")
    val runtime = integer("runtime")
    val seasonNumber = integer("season_number")
    val stillPath = text("still_path")
    val order = integer("order")
    val season = reference("ref_season", SeasonsEntity)
    val show = reference("ref_show", ShowEntity)
    val lastSyncDate = datetime("last_sync")
}

class EpisodesDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, EpisodesDAO>(EpisodesEntity)

    var overview by EpisodesEntity.overview
    var runtime by EpisodesEntity.runtime
    var stillPath by EpisodesEntity.stillPath
    var number by EpisodesEntity.number
    var name by EpisodesEntity.name
    var airDate by EpisodesEntity.airDate
    var path by EpisodesEntity.path
    var seasonNumber by EpisodesEntity.seasonNumber
    var productionCode by EpisodesEntity.productionCode
    var order by EpisodesEntity.order
    var lastSyncDate by EpisodesEntity.lastSyncDate

    var season by SeasonsDAO referencedOn EpisodesEntity.season
    var show by ShowDAO referencedOn EpisodesEntity.show
}

fun EpisodesEntity.batchInsertEpisodes(
    showId: ShowID,
    seasonID: SeasonID,
    data: Iterable<TMDBEpisode>,
): List<ResultRow> = batchInsert(data, shouldReturnGeneratedValues = true) {
    this[number] = it.episodeNumber
    this[airDate] = it.airDate ?: mockLocalDate
    this[name] = it.name
    this[overview] = it.overview
    this[productionCode] = it.productionCode
    this[runtime] = it.runtime ?: 0
    this[seasonNumber] = it.seasonNumber
    this[stillPath] = it.stillPath ?: "" // TODO
    this[order] = it.order ?: -1 //TODO
    this[season] = seasonID
    this[show] = showId
    this[lastSyncDate] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
}



