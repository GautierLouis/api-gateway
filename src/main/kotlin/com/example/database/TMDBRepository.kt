package com.example.database

import com.example.database.entity.*
import com.example.model.*
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert

class TMDBRepository : TMDBRepositoryInteraction {

    override suspend fun findShow(cleanName: String): Show? = query {
        val show = ShowDAO.find(ShowEntity.findBy eq cleanName)
            .singleOrNull()
            ?.toModel()

        show?.apply {
            externalId = ShowExternalIdsDAO.find(ShowExternalIdsEntity.show eq show.id).singleOrNull()?.toModel()
            seasons = SeasonsDAO.find(SeasonsEntity.show eq show.id).map { it.toModel() }
            episode = EpisodesDAO.find(EpisodesEntity.show eq show.id).map { it.toModel() }
        }
    }

    override suspend fun findSeason(id: ShowID, seasonNumber: Int): Season? {
        val matchNumber = SeasonsEntity.number eq seasonNumber
        val matchId = SeasonsEntity.show eq id
        val predicate = matchNumber and matchId
        return SeasonsDAO.find(predicate).singleOrNull()?.toModel()
    }

    override suspend fun findEpisode(seasonID: SeasonID, episodeNumber: Int): Episode? {
        val matchNumber = EpisodesEntity.number eq episodeNumber
        val matchId = EpisodesEntity.season eq seasonID
        val predicate = matchNumber and matchId
        return EpisodesDAO.find(predicate).singleOrNull()?.toModel()
    }

    override suspend fun insertShow(
        tmdbShow: TMDBShow,
        externalId: TMDBShowExternalIds,
        cleanName: String,
    ): Result<Show> = query {

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
        }

        val externalIds = ShowExternalIdsDAO.new {
            this.show = entity
            tvdbId = externalId.tvdbId
            tvrageId = externalId.tvrageId
            wikidataId = externalId.wikidataId
            freebaseId = externalId.freebaseId
            freebaseMid = externalId.freebaseMid
            facebookId = externalId.facebookId
            instagramId = externalId.instagramId
        }

        entity.externalsIds = externalIds

        return@query Result.success(entity.toModel())

    }

    override suspend fun batchInsertSeasons(showId: ShowID, season: List<TMDBSeason>): List<Season> = query {
        SeasonsEntity.batchInsert(season, shouldReturnGeneratedValues = true) {
            this[SeasonsEntity.number] = it.seasonNumber
            this[SeasonsEntity.poster] = it.posterPath ?: "" // TODO
            this[SeasonsEntity.airDate] = it.airDate ?: LocalDate.fromEpochDays(0) //TODO
            this[SeasonsEntity.overview] = it.overview
            this[SeasonsEntity.show] = showId
        }.map { SeasonsDAO.findById(it[SeasonsEntity.id])!!.toModel() }
    }

    override suspend fun batchInsertEpisodes(
        showId: ShowID,
        seasons: List<Season>,
        episodes: List<TMDBEpisode>,
        videoFile: VideoFile
    ) = query {
        EpisodesEntity.batchInsert(episodes, shouldReturnGeneratedValues = true) {
            val seasonId = seasons.first { s -> s.number == it.seasonNumber }.id

            val filePath =
                if (videoFile.match(it.seasonNumber, it.episodeNumber)) videoFile.file.absolutePath
                else null

            this[EpisodesEntity.number] = it.episodeNumber
            this[EpisodesEntity.path] = videoFile.file.path
            this[EpisodesEntity.airDate] = it.airDate
            this[EpisodesEntity.name] = it.name
            this[EpisodesEntity.overview] = it.overview
            this[EpisodesEntity.productionCode] = it.productionCode
            this[EpisodesEntity.runtime] = it.runtime
            this[EpisodesEntity.seasonNumber] = it.seasonNumber
            this[EpisodesEntity.stillPath] = it.stillPath ?: "" // TODO
            this[EpisodesEntity.order] = it.order ?: -1 //TODO
            this[EpisodesEntity.season] = seasonId
            this[EpisodesEntity.show] = showId
            this[EpisodesEntity.path] = filePath
        }.map { EpisodesDAO.findById(it[EpisodesEntity.id])!!.toModel() }
    }
}