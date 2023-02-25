package com.example.usecases

import com.example.database.TMDBRepository
import com.example.file.VideoFileExport


class SyncUseCase(
    private val repository: TMDBRepository,
    private val syncCompleteShow: SyncCompleteShow,
    private val syncSeason: SyncSeasonUseCase,
    private val syncEpisode: SyncEpisodeUseCase
) {

    suspend fun execute(file: VideoFileExport) {
        val show = repository.findShow(file.cleanName)

        // Add if this a new show : TODO Must update episode file path. TODO 2 Do data validation - missing required fields
        if (show == null) {
            syncCompleteShow.execute(file.cleanName)
            return
        }

        if (file.seasonNumber == null) {
            //TODO
            return
        }

        val season = repository.findSeason(show.id, file.seasonNumber)

        if (season == null) {
            syncSeason.execute(show.id, show.externalId.tmdbId!!, file.seasonNumber)
            return
        }

        if (file.episodeNumber == null) {
            //TODO
            return
        }

        val episode = repository.findEpisode(season.id, file.seasonNumber)

        if (episode == null) {
            syncEpisode.execute(show, season.id, file)
            return
        }
    }
}
