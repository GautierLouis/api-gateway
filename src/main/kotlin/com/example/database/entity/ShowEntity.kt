package com.example.database.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object ShowEntity : LongIdTable("show") {
    // Data from API
    val name = varchar128("name")
    val firstAired = date("first_aired")
    val lastAired = date("last_aired")
    val nextAired = date("next_aired").nullable()
    val poster = varchar128("poster")
    val originalCountry = varchar("original_country", 4)
    val originalLanguage = varchar("original_language", 4)
    val status = varchar128("status")
    val averageRuntime = double("average_runtime")
    val overview = text("overview")
    val numberOfSeasons = integer("number_of_seasons")
    val numberOfEpisodes = integer("number_of_episodes")
    val externalIds = reference("ref_external_id", ShowExternalIdsEntity)

    // Local info
    val findBy = varchar128("find_by")
    val lastSyncDate = datetime("last_sync")
}

class ShowDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, ShowDAO>(ShowEntity)

    var name by ShowEntity.name
    var firstAired by ShowEntity.firstAired
    var lastAired by ShowEntity.lastAired
    var nextAired by ShowEntity.nextAired
    var poster by ShowEntity.poster
    var originalCountry by ShowEntity.originalCountry
    var originalLanguage by ShowEntity.originalLanguage
    var status by ShowEntity.status
    var averageRuntime by ShowEntity.averageRuntime
    var overview by ShowEntity.overview
    var numberOfSeasons by ShowEntity.numberOfSeasons
    var numberOfEpisodes by ShowEntity.numberOfEpisodes
    var findBy by ShowEntity.findBy
    var lastSyncDate by ShowEntity.lastSyncDate

    var externalsIds by ShowExternalIdsDAO referencedOn ShowEntity.externalIds
    val seasons by SeasonsDAO referrersOn SeasonsEntity.show
    val episodes by EpisodesDAO referrersOn EpisodesEntity.show
}