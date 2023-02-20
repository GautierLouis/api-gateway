package com.example.database.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object EpisodesEntity : Table("episode") {
    val dbId = long("db_id").autoIncrement()
    override val primaryKey = PrimaryKey(dbId)
    val number = integer("number")
    val path = text("path")
    val airDate = date("air_date")
    val name = varchar128("name")
    val overview = text("overview")
    val productionCode = varchar128("production_code")
    val runtime = integer("runtime")
    val seasonNumber = integer("season_number")
    val stillPath = text("still_path")
    val order = integer("order")
}


