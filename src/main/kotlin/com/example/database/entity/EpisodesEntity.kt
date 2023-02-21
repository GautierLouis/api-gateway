package com.example.database.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

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

    var season by SeasonsDAO referencedOn EpisodesEntity.season
    var show by ShowDAO referencedOn EpisodesEntity.show
}



