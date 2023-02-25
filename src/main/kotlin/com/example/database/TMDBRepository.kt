package com.example.database

import com.example.database.entity.*
import com.example.model.*
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason
import com.example.remote.tmdb.model.TMDBShow
import com.example.utils.mockLocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.update

class TMDBRepository : TMDBRepositoryInteraction {

    override suspend fun findShow(cleanName: String): Show? = query {
        ShowDAO.find(ShowEntity.findBy eq cleanName)
            .singleOrNull()
            ?.toModel()
    }

    override suspend fun findSeason(id: ShowID, seasonNumber: Int): Season? = query {
        val matchNumber = SeasonsEntity.number eq seasonNumber
        val matchId = SeasonsEntity.show eq id
        val predicate = matchNumber and matchId
        SeasonsDAO.find(predicate).singleOrNull()?.toModel()
    }

    override suspend fun findEpisode(seasonID: SeasonID, episodeNumber: Int): Episode? = query {
        val matchNumber = EpisodesEntity.number eq episodeNumber
        val matchId = EpisodesEntity.season eq seasonID
        val predicate = matchNumber and matchId
        EpisodesDAO.find(predicate).singleOrNull()?.toModel()
    }

    override suspend fun findEpisodes(showId: ShowID, seasonNumber: Int, episodeNumber: Int): Episode? {
        val matchEpisodeNumber = EpisodesEntity.number eq episodeNumber
        val matchSeasonNumber = EpisodesEntity.seasonNumber eq seasonNumber
        val matchShowId = EpisodesEntity.show eq showId

        return EpisodesDAO.find {
            matchEpisodeNumber and matchSeasonNumber and matchShowId
        }.singleOrNull()?.toModel()
    }

    override suspend fun insertEpisode(
        showId: ShowID,
        seasonID: SeasonID,
        episode: TMDBEpisode,
        filePath: String
    ): Result<Episode> = query {
        try {
            val entity = EpisodesDAO.new {
                number = episode.episodeNumber
                airDate = episode.airDate ?: mockLocalDate
                name = episode.name
                overview = episode.overview
                productionCode = episode.productionCode
                runtime = episode.runtime ?: 0
                this.seasonNumber = episode.seasonNumber
                stillPath = episode.stillPath ?: "" // TODO
                order = episode.order ?: -1 //TODO
                season = SeasonsDAO.findById(seasonID)!!
                show = ShowDAO.findById(showId)!!
                path = filePath
                lastSyncDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            }

            Result.success(entity.toModel())

        } catch (e: Exception) {
            Result.failure(e)
        }

    }


    override suspend fun insertCompleteShow(tmdbShow: TMDBShow, cleanName: String): Result<Show> = query {
        try {
            val ids = ShowExternalIdsDAO.new {
                tvdbId = tmdbShow.externalIds!!.tvdbId
                tvrageId = tmdbShow.externalIds.tvrageId
                wikidataId = tmdbShow.externalIds.wikidataId
                freebaseId = tmdbShow.externalIds.freebaseId
                freebaseMid = tmdbShow.externalIds.freebaseMid
                facebookId = tmdbShow.externalIds.facebookId
                instagramId = tmdbShow.externalIds.instagramId
            }

            val entity = ShowDAO.new {
                name = tmdbShow.name
                firstAired = tmdbShow.firstAirDate
                lastAired = tmdbShow.lastAirDate
                nextAired = tmdbShow.nextEpisodeToAir?.airDate
                poster = tmdbShow.posterPath ?: "" //TODO("Handle null state")
                originalCountry = tmdbShow.originCountry.firstOrNull() ?: "" // TODO("Handle null state")
                originalLanguage = tmdbShow.originalLanguage
                status = tmdbShow.status
                averageRuntime = tmdbShow.episodeRunTime.average()
                overview = tmdbShow.overview
                findBy = cleanName
                numberOfSeasons = tmdbShow.realSeasons.count()
                numberOfEpisodes = tmdbShow.numberOfEpisodes
                externalsIds = ids
                lastSyncDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            }

            ShowExternalIdsEntity.update(
                where = { ShowExternalIdsEntity.tmdbId eq tmdbShow.tmdbId },
                body = { it[show] = entity.id }
            )

            SeasonsEntity.batchInsert(tmdbShow.realSeasons, shouldReturnGeneratedValues = true) {
                this[SeasonsEntity.number] = it.seasonNumber
                this[SeasonsEntity.poster] = it.posterPath ?: "" // TODO
                this[SeasonsEntity.airDate] = it.airDate ?: LocalDate.fromEpochDays(0) //TODO
                this[SeasonsEntity.overview] = it.overview
                this[SeasonsEntity.show] = entity.id
                this[SeasonsEntity.lastSyncDate] = Clock.System.now().toLocalDateTime(TimeZone.UTC)

            }.forEach { row ->
                val seasonId = row[SeasonsEntity.id]
                EpisodesEntity.batchInsertEpisodes(
                    entity.id.value,
                    seasonId.value,
                    tmdbShow.seasons.mapNotNull { it.episodes }.flatten(),
                ).map { EpisodesDAO.findById(it[EpisodesEntity.id])!!.toModel() }
            }

            val completeShow = ShowDAO.findById(entity.id)

            if (completeShow == null) {
                Result.failure(Exception("Unable to get new show"))
            } else {
                Result.success(completeShow.toModel())
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateEpisodePath(episodeID: EpisodeID, filePath: String) = query {
        EpisodesDAO.findById(episodeID)?.path = filePath
    }

    override suspend fun insertSeason(showId: ShowID, tmdbSeason: TMDBSeason): Result<Season> = query {
        try {
            val entity = SeasonsDAO.new {
                number = tmdbSeason.seasonNumber
                poster = tmdbSeason.posterPath ?: "" // TODO
                airDate = tmdbSeason.airDate ?: mockLocalDate
                overview = tmdbSeason.overview
                show = ShowDAO.findById(showId)!!
                lastSyncDate = Clock.System.now().toLocalDateTime(TimeZone.UTC)

            }

            val episodesEntity = if (tmdbSeason.episodes.isNullOrEmpty()) {
                insertEpisodes(showId, entity.id.value, tmdbSeason.episodes ?: emptyList())
            } else Result.success(emptyList())

            episodesEntity.map { entity.toModel() }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun insertEpisodes(
        showId: ShowID,
        seasonID: SeasonID,
        tmdbEpisodes: List<TMDBEpisode>
    ): Result<List<Episode>> = query {
        try {
            val episodes = EpisodesEntity
                .batchInsertEpisodes(showId, seasonID, tmdbEpisodes)
                .map { EpisodesDAO.findById(it[EpisodesEntity.id])!!.toModel() }

            Result.success(episodes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}