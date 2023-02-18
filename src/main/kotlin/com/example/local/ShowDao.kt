package com.example.local

import com.example.model.ShowExternalId
import com.example.model.VideoFile
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import java.io.File

interface ShowDao {
    suspend fun searchShow(cleanName: String): Int?
    suspend fun insertShow(show: TMDBShow, videoFile: VideoFile): Result<ShowExternalId>
    suspend fun insertExternalIds(ids: TMDBShowExternalIds): Result<ShowExternalId>
}

