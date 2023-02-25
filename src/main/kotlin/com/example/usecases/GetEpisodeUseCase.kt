package com.example.usecases

import com.example.database.TMDBRepository
import com.example.file.VideoFileExport
import com.example.model.*
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.utils.next

class SyncEpisodeUseCase(
    private val getEpisodeUseCase: GetEpisodeUseCase,
    private val saveEpisodeUseCase: SaveEpisodeUseCase,
) {

    suspend fun execute(
        show: Show,
        seasonID: SeasonID,
        videoFile: VideoFileExport,
    ): Result<Episode> {
        return getEpisodeUseCase.execute(
            show.externalId.tmdbId!!,
            videoFile.seasonNumber!!,
            videoFile.episodeNumber!!
        ).next {
            saveEpisodeUseCase.execute(
                show.id, seasonID, it, videoFile.path
            )
        }
    }
}

class GetEpisodeUseCase(
    private val service: TMDBService
) {
    suspend fun execute(tmdbId: TMDBShowId, seasonNumber: Int, episodeNumber: Int): Result<TMDBEpisode> {
        return service.getEpisode(tmdbId, seasonNumber, episodeNumber)
    }
}

class SaveEpisodeUseCase(
    private val repository: TMDBRepository
) {

    suspend fun execute(
        showId: ShowID,
        seasonID: SeasonID,
        tmdbEpisode: TMDBEpisode,
        filePath: String
    ): Result<Episode> {
        return repository.insertEpisode(showId, seasonID, tmdbEpisode, filePath)
    }
}