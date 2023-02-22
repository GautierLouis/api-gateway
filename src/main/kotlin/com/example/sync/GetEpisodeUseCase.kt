package com.example.sync

import com.example.database.TMDBRepository
import com.example.model.SeasonID
import com.example.model.ShowID
import com.example.model.TMDBShowId
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService

class GetEpisodeUseCase(
    private val service: TMDBService,
    private val repository: TMDBRepository,
) {
    suspend fun execute(
        showId: ShowID,
        seasonID: SeasonID,
        tmdbId: TMDBShowId,
        videoFile: VideoFile
    ) {
        service.getEpisode(tmdbId, videoFile.seasonNumber, videoFile.episodeNumber).onSuccess {
            repository.insertEpisode(showId, seasonID, it, videoFile)
        }
    }
}