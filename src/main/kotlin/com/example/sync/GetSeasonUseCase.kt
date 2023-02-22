package com.example.sync

import com.example.database.TMDBRepository
import com.example.model.ShowID
import com.example.model.TMDBShowId
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService

class GetSeasonUseCase(
    private val service: TMDBService,
    private val repository: TMDBRepository,
) {
    suspend fun execute(showId: ShowID, id: TMDBShowId, videoFile: VideoFile) {
        service.getSeason(id, videoFile.seasonNumber).onSuccess {
            val newSeason = repository.batchInsertSeasons(showId, listOf(it))
            repository.batchInsertEpisodes(showId, newSeason, it.episodes, videoFile)
        }
    }
}