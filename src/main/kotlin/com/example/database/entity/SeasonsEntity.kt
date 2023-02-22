package com.example.database.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

object SeasonsEntity : LongIdTable("seasons") {
    val number = integer("number")
    val poster = varchar128("poster")
    val airDate = date("air_date")
    val overview = text("overview")
    val show = reference("ref_show", ShowEntity)
}

class SeasonsDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, SeasonsDAO>(SeasonsEntity)

    var number by SeasonsEntity.number
    var poster by SeasonsEntity.poster
    var airDate by SeasonsEntity.airDate
    var overview by SeasonsEntity.overview
    var show by ShowDAO referencedOn SeasonsEntity.show
    val episodes by EpisodesDAO referrersOn EpisodesEntity.season
}