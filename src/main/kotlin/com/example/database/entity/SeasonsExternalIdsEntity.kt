package com.example.database.entity

import org.jetbrains.exposed.sql.Table

// Not Used
object SeasonsExternalIdsEntity : Table("seasons_external_id") {
    val dbId = integer("db_id").autoIncrement()
    override val primaryKey = PrimaryKey(dbId)
    val seasonId = reference("season_id", SeasonsEntity.dbId).uniqueIndex()
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