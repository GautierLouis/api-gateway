package com.example.database

import com.example.model.ShowExternalId
import com.example.model.TMDBShowId
import com.example.model.VideoFile
import com.example.remote.tmdb.model.EpisodeGroup
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import java.io.File

interface ShowDao {
    suspend fun findShow(cleanName: String): Long?
    suspend fun insertShow(show: TMDBShow, videoFile: VideoFile): Result<TMDBShowId>
    suspend fun insertExternalIds(ids: TMDBShowExternalIds): Result<ShowExternalId>
    suspend fun insertEpisodes(episodesGroup: List<EpisodeGroup>, file: File): Result<Boolean>
}

