package com.example.database.entity

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ShowExternalIdsEntity : LongIdTable("show_external_id") {
    val show = reference("show", ShowEntity).uniqueIndex()
    val tmdbId = long("tmdb_id").uniqueIndex().nullable()
    val imdbId = varchar128("imdb_id").nullable()
    val tvdbId = integer("tvdb_id").nullable()
    val wikidataId = varchar128("wikidata_id").nullable()
    val freebaseId = varchar128("freebase_dd").nullable()
    val freebaseMid = varchar128("freebase_id").nullable()
    val tvrageId = integer("tvrage_id").nullable()
    val facebookId = varchar128("facebook_id").nullable()
    val instagramId = varchar128("instagram_id").nullable()
}

class ShowExternalIdsDAO(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, ShowExternalIdsDAO>(ShowExternalIdsEntity)

    var tmdbId by ShowExternalIdsEntity.tmdbId
    var imdbId by ShowExternalIdsEntity.imdbId
    var tvdbId by ShowExternalIdsEntity.tvdbId
    var wikidataId by ShowExternalIdsEntity.wikidataId
    var freebaseId by ShowExternalIdsEntity.freebaseId
    var freebaseMid by ShowExternalIdsEntity.freebaseMid
    var tvrageId by ShowExternalIdsEntity.tvrageId
    var facebookId by ShowExternalIdsEntity.facebookId
    var instagramId by ShowExternalIdsEntity.instagramId
    var show by ShowDAO referencedOn ShowEntity.id
}