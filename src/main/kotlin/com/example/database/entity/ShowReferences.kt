package com.example.database.entity

import org.jetbrains.exposed.sql.Table

object ShowReferences : Table("ref_show_season") {
    val showId = reference("show_id", ShowEntity.dbId)
    val seasonId = reference("season_id", SeasonsEntity.dbId)
    val episodeId = reference("episode_id", EpisodesEntity.dbId)
    override val primaryKey = PrimaryKey(arrayOf(showId, seasonId, episodeId))
}