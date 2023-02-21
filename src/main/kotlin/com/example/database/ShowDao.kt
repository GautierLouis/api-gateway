package com.example.database

import com.example.model.*
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds

interface TMDBRepositoryInteraction {
    suspend fun findShow(cleanName: String): Show?
    suspend fun findSeason(id: ShowID, seasonNumber: Int): Season?
    suspend fun findEpisode(seasonID: SeasonID, episodeNumber: Int): Episode?

    suspend fun insertShow(tmdbShow: TMDBShow, videoFile: VideoFile): Result<Show>
    suspend fun insertExternalIds(ids: TMDBShowExternalIds): Result<ShowExternalId>
    suspend fun insertEpisodes(episodes: List<TMDBEpisode>, videoFile: VideoFile): Result<Boolean>
}

