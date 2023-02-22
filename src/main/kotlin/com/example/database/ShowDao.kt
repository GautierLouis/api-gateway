package com.example.database

import com.example.database.entity.EpisodesDAO
import com.example.model.*
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds

interface TMDBRepositoryInteraction {
    suspend fun findShow(cleanName: String): Show?
    suspend fun findSeason(id: ShowID, seasonNumber: Int): Season?
    suspend fun findEpisode(seasonID: SeasonID, episodeNumber: Int): Episode?

    suspend fun insertShow(
        tmdbShow: TMDBShow,
        externalId: TMDBShowExternalIds,
        cleanName: String,
    ): Result<Show>

    suspend fun batchInsertSeasons(showId: ShowID, season: List<TMDBSeason>): List<Season>

    suspend fun batchInsertEpisodes(
        showId: ShowID,
        seasons: List<Season>,
        episodes: List<TMDBEpisode>,
        videoFile: VideoFile
    ): List<Episode>

    suspend fun insertEpisode(
        showId: ShowID,
        seasonID: SeasonID,
        episode: TMDBEpisode,
        videoFile: VideoFile
    ): EpisodesDAO
}

