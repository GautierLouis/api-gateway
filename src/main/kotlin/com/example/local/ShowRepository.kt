package com.example.local

import com.example.local.entity.ShowEntity
import com.example.local.entity.ShowIdEntity
import com.example.model.Show
import com.example.model.ShowExternalId
import com.example.model.VideoFile
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.update

fun <T : Table, M> T.insertSingle(serializer: (ResultRow) -> M, body: T.(InsertStatement<Number>) -> Unit): M? =
    InsertStatement<Number>(this)
        .apply {
            body(this)
            execute(TransactionManager.current())
        }.resultedValues
        ?.firstOrNull()
        ?.let(serializer)


class ShowRepository : ShowDao {

    private fun resultRowToShow(row: ResultRow) = Show(
        row[ShowEntity.dbId],
        row[ShowEntity.name],
        row[ShowEntity.firstAired],
        row[ShowEntity.lastAired],
        row[ShowEntity.nextAired],
        row[ShowEntity.poster],
        row[ShowEntity.originalCountry],
        row[ShowEntity.originalLanguage],
        row[ShowEntity.status],
        row[ShowEntity.averageRuntime],
        row[ShowEntity.overview]
    )

    private fun resultRowToShowID(row: ResultRow) = ShowExternalId(
        row[ShowIdEntity.dbId],
        row[ShowIdEntity.showId],
        row[ShowIdEntity.tmdbId],
        row[ShowIdEntity.imdbId],
        row[ShowIdEntity.tvdbId],
        row[ShowIdEntity.wikidataId],
        row[ShowIdEntity.freebaseId],
        row[ShowIdEntity.freebaseMid],
        row[ShowIdEntity.tvrageId],
        row[ShowIdEntity.facebookId],
        row[ShowIdEntity.instagramId],
    )

    override suspend fun searchShow(cleanName: String): Int? = query {
        ShowEntity.select(ShowEntity.findBy eq cleanName)
            .singleOrNull()
            ?.get(ShowEntity.dbId)
    }

    override suspend fun insertShow(show: TMDBShow, videoFile: VideoFile) = query {
        val newShow = ShowEntity.insertSingle(::resultRowToShow) {
            it[name] = show.name
            it[firstAired] = show.firstAirDate
            it[lastAired] = show.lastAirDate
            it[nextAired] = show.nextEpisodeToAir?.airDate
            it[poster] = show.posterPath ?: "" //TODO("Handle null state")
            it[originalCountry] = show.originCountry.firstOrNull() ?: "" // TODO("Handle null state")
            it[originalLanguage] = show.originalLanguage
            it[status] = show.status
            it[averageRuntime] = show.episodeRunTime.average()
            it[overview] = show.overview
            it[path] = videoFile.file.path
            it[findBy] = videoFile.showName
        }

        val showID = if (newShow != null) {
            ShowIdEntity.insertSingle(::resultRowToShowID) {
                it[showId] = newShow.id
                it[tmdbId] = show.id
            }
        } else null

        return@query if (showID != null) {
            Result.success(showID)
        } else {
            Result.failure(Exception("Unable to store show ${show.id}"))
        }
    }

    override suspend fun insertExternalIds(ids: TMDBShowExternalIds) = query {
        val whereClause = ShowIdEntity.tmdbId eq ids.id
        val rowUpdated = ShowIdEntity.update(
            where = { whereClause },
            body = {
                it[imdbId] = ids.imdbId
                it[tvdbId] = ids.tvdbId
                it[tvrageId] = ids.tvrageId
                it[wikidataId] = ids.wikidataId
                it[freebaseId] = ids.freebaseId
                it[freebaseMid] = ids.freebaseMid
                it[facebookId] = ids.facebookId
                it[instagramId] = ids.instagramId
            }
        )

        val showIDs: ShowExternalId? = ShowIdEntity
            .select(whereClause)
            .singleOrNull()
            ?.let(::resultRowToShowID)

        return@query if (rowUpdated > 0 && showIDs != null) {
            Result.success(showIDs)
        } else Result.failure(Exception("Unable to update external ids for ${ids.id}"))
    }

}