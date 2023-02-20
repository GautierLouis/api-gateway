package com.example.database.entity

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.date

object SeasonsEntity : Table("seasons") {
    val dbId = long("db_id").autoIncrement()
    override val primaryKey = PrimaryKey(dbId)
    val number = integer("number")
    val poster = varchar128("poster")
    val airDate = date("air_date")
    val overview = text("overview")
}

