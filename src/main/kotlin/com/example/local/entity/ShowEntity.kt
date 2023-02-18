package com.example.local.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object ShowEntity : Table("show") {
    // Identifier
    val dbId = integer("db_id").autoIncrement()
    override val primaryKey = PrimaryKey(dbId)

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
    val overview = text("overvieew")

    // Local info
    val path = text("path")
    val findBy = varchar128("find_by")
}