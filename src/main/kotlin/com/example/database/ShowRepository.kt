package com.example.database

import com.example.database.entity.*
import com.example.model.*
import com.example.remote.tmdb.model.EpisodeGroup
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.io.File

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
        row[ShowEntity.overview],
        row[ShowEntity.numberOfSeason],
        row[ShowEntity.numberOfEpisodes]
    )

    private fun resultRowToShowID(row: ResultRow) = ShowExternalId(
        row[ShowExternalIdsEntity.dbId],
        row[ShowExternalIdsEntity.showId],
        row[ShowExternalIdsEntity.tmdbId],
        row[ShowExternalIdsEntity.imdbId],
        row[ShowExternalIdsEntity.tvdbId],
        row[ShowExternalIdsEntity.wikidataId],
        row[ShowExternalIdsEntity.freebaseId],
        row[ShowExternalIdsEntity.freebaseMid],
        row[ShowExternalIdsEntity.tvrageId],
        row[ShowExternalIdsEntity.facebookId],
        row[ShowExternalIdsEntity.instagramId],
    )

    private fun resultRowToSeason(row: ResultRow) = Season(
        row[SeasonsEntity.dbId],
        row[SeasonsEntity.number],
        row[SeasonsEntity.poster],
        row[SeasonsEntity.airDate],
        row[SeasonsEntity.overview],
    )

    private fun resultRowToEpisode(row: ResultRow) = Episode(
        row[EpisodesEntity.dbId],
        row[EpisodesEntity.overview],
        row[EpisodesEntity.runtime],
        row[EpisodesEntity.stillPath],
        row[EpisodesEntity.number],
        row[EpisodesEntity.name],
        row[EpisodesEntity.airDate],
        row[EpisodesEntity.path],
        row[EpisodesEntity.seasonNumber],
    )

    override suspend fun findShow(cleanName: String): Long? = query {
        ShowEntity.select(ShowEntity.findBy eq cleanName)
            .singleOrNull()
            ?.get(ShowEntity.dbId)
    }

    override suspend fun insertExternalIds(ids: TMDBShowExternalIds) = query {
        val showExternalId = ShowExternalIdsEntity.insertSingle(::resultRowToShowID) {
            it[imdbId] = ids.imdbId
            it[tvdbId] = ids.tvdbId
            it[tvrageId] = ids.tvrageId
            it[wikidataId] = ids.wikidataId
            it[freebaseId] = ids.freebaseId
            it[freebaseMid] = ids.freebaseMid
            it[facebookId] = ids.facebookId
            it[instagramId] = ids.instagramId
        } ?: return@query Result.failure(Exception("Unable to update external ids for ${ids.id}"))

        Result.success(showExternalId)
    }

    override suspend fun insertShow(show: TMDBShow, videoFile: VideoFile): Result<TMDBShowId> = query {
        val entity = ShowEntity.insertSingle(::resultRowToShow) {
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
            it[numberOfSeason] = show.realSeasons.count()
        } ?: return@query Result.failure(Exception("Unable to store show ${show.tmdbId}"))

        ShowExternalIdsEntity.update(
            where = { ShowExternalIdsEntity.tmdbId eq show.tmdbId },
            body = { it[showId] = entity.id })

        SeasonsEntity.batchInsert(show.realSeasons) {
            this[SeasonsEntity.number] = it.seasonNumber
            this[SeasonsEntity.poster] = it.posterPath ?: "" // TODO()
            this[SeasonsEntity.airDate] = it.airDate ?: LocalDate.fromEpochDays(0) // TODO
            this[SeasonsEntity.overview] = it.overview
        }

        return@query Result.success(show.tmdbId)

    }

    override suspend fun insertEpisodes(episodesGroup: List<EpisodeGroup>, file: File): Result<Boolean> = query {

        val episodes = episodesGroup
            .flatMap { it.episodes }
            .filter { it.productionCode.isNotBlank() } // Remove Special Episodes

        val entities = EpisodesEntity.batchInsert(episodes, shouldReturnGeneratedValues = true) {
            this[EpisodesEntity.number] = it.episodeNumber
            this[EpisodesEntity.path] = file.path
            this[EpisodesEntity.airDate] = it.airDate
            this[EpisodesEntity.name] = it.name
            this[EpisodesEntity.overview] = it.overview
            this[EpisodesEntity.productionCode] = it.productionCode
            this[EpisodesEntity.runtime] = it.runtime
            this[EpisodesEntity.seasonNumber] = it.seasonNumber
            this[EpisodesEntity.stillPath] = it.stillPath ?: ""
            this[EpisodesEntity.order] = it.order
        }.map { resultRowToEpisode(it) }

        val externalIds = ShowExternalIdsEntity
            .select(ShowExternalIdsEntity.tmdbId eq episodes.first().tmdbId)
            .singleOrNull()?.let(::resultRowToShowID)
            ?: return@query Result.failure(Exception(""))

        ShowEntity.update(
            where = { ShowEntity.dbId eq externalIds.showId },
            body = {
                it[ShowEntity.numberOfEpisodes] = episodes.count()
            }
        )

        val seasons = Join(
            SeasonsEntity, ShowSeasonReference,
            onColumn = SeasonsEntity.dbId,
            otherColumn = ShowSeasonReference.seasonId,
        ).select(ShowEntity.dbId eq externalIds.showId)
            .map { resultRowToSeason(it) }

        seasons.forEach { season ->
            val episodesInSeason = entities.filter { it.seasonNumber == season.number }
            ShowSeasonReference.batchInsert(episodesInSeason) {
                this[ShowSeasonReference.showId] = externalIds.showId
                this[ShowSeasonReference.seasonId] = season.id
                this[ShowSeasonReference.episodeId] = it.id
            }
        }

        Result.success(true)
    }
}