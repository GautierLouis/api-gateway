package com.example.database

import com.example.database.entity.*
import com.example.model.Show
import com.example.model.VideoFile
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select

class TMDBRepository : MDBRepositoryInteraction {

    override suspend fun findShow(cleanName: String): Long? = query {
        ShowDAO.find(ShowEntity.findBy eq cleanName)
            .singleOrNull()
            ?.id
            ?.value
    }

    override suspend fun insertShow(tmdbShow: TMDBShow, videoFile: VideoFile): Result<Show> = query {
        val a = ShowDAO.new {
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
            path = videoFile.file.path
            findBy = videoFile.showName
            numberOfSeasons = tmdbShow.realSeasons.count()
            numberOfEpisodes = tmdbShow.numberOfEpisodes
        }

        ShowExternalIdsDAO.new {
            tmdbId = tmdbShow.tmdbId
            this.show = a
        }

        tmdbShow.realSeasons.map {
            SeasonsDAO.new {
                number = it.seasonNumber
                poster = it.posterPath ?: ""
                airDate = it.airDate ?: LocalDate.fromEpochDays(0)
                overview = it.overview
                show = a
            }
        }

        return@query Result.success(a.toModel())

    }

    override suspend fun insertExternalIds(ids: TMDBShowExternalIds) = query {
        val clause = ShowExternalIdsEntity.tmdbId eq ids.tmdbId

        val a = ShowExternalIdsDAO.find(clause).singleOrNull()
            ?.apply {
                tvdbId = ids.tvdbId
                tvrageId = ids.tvrageId
                wikidataId = ids.wikidataId
                freebaseId = ids.freebaseId
                freebaseMid = ids.freebaseMid
                facebookId = ids.facebookId
                instagramId = ids.instagramId
            } ?: return@query Result.failure(Exception("Unable to update external ids for ${ids.tmdbId}"))

        Result.success(a.toModel())
    }

    override suspend fun insertEpisodes(
        episodes: List<TMDBEpisode>,
        videoFile: VideoFile
    ): Result<Boolean> = query {
        episodes.map { episode ->
            val showId = Join(
                ShowExternalIdsEntity, ShowEntity,
                onColumn = ShowExternalIdsEntity.show,
                otherColumn = ShowEntity.id
            ).select {
                ShowExternalIdsEntity.tmdbId eq episode.tmdbId
            }.singleOrNull()?.let { it[ShowEntity.id] }
                ?: return@query Result.failure(Exception(""))

            val seasonResult = SeasonsDAO.find(SeasonsEntity.show eq showId).first()
            val filePath =
                if (videoFile.episodeNumber == episode.episodeNumber && videoFile.seasonNumber == episode.seasonNumber)
                    videoFile.file.path
                else null

            EpisodesDAO.new {
                number = episode.episodeNumber
                path = filePath
                airDate = episode.airDate
                name = episode.name
                overview = episode.overview
                productionCode = episode.productionCode
                runtime = episode.runtime
                seasonNumber = episode.seasonNumber
                stillPath = episode.stillPath ?: ""
                order = episode.order ?: 0
                show = ShowDAO.findById(showId)!!
                season = seasonResult
            }
        }

        Result.success(true)
    }
}