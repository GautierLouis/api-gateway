package com.example.database

import com.example.model.Show
import com.example.model.ShowExternalId
import com.example.model.VideoFile
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds

interface MDBRepositoryInteraction {
    suspend fun findShow(cleanName: String): Long?
    suspend fun insertShow(tmdbShow: TMDBShow, videoFile: VideoFile): Result<Show>
    suspend fun insertExternalIds(ids: TMDBShowExternalIds): Result<ShowExternalId>
    suspend fun insertEpisodes(episodes: List<TMDBEpisode>, videoFile: VideoFile): Result<Boolean>
}

