package com.example.database.entity

import org.jetbrains.exposed.sql.Table

fun Table.varchar128(name: String) = varchar(name, 128)

object TokenEntity : Table("token") {
    val name = varchar128("name")
    val value = text("value")
    override val primaryKey = PrimaryKey(name)
}