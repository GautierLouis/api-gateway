package com.example.local.entity

import org.jetbrains.exposed.sql.Table

fun Table.varchar128(name: String) = varchar(name, 128)

object TokenEntity : Table("token") {
    val name = varchar128("name")
    val value = text("value")
    override val primaryKey = PrimaryKey(name)
}

//object SeasonsID : Table() {
//    val id = reference("id", Seasons.id).uniqueIndex()
//    val tmdbId = long("tmdb_id").uniqueIndex()
//}
//
//object Seasons : Table() {
//    val dbId = integer("db_id").autoIncrement()
//    val number = integer("number")
//    val poster = varchar128("poster")
//    val ids = reference("id", SeasonsID.id)
//    val showId = reference("id", Shows.dbId)
//
//}

//object Episodes: Table() {
//    val id = integer("id").autoIncrement()
//    val number = integer("number")
//    val poster = varchar128("poster")
//    val ids = reference("id", Seasons.id)
//
//}