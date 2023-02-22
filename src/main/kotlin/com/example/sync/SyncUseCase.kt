package com.example.sync

import com.example.database.TMDBRepository
import com.example.model.SeasonID
import com.example.model.Show
import com.example.model.VideoFile
import com.example.usecases.GetCompleteShowUseCase
import com.example.usecases.SearchShowUseCase
import com.example.utils.next


class SyncUseCase(
    private val repository: TMDBRepository,
    private val searchShowUseCase: SearchShowUseCase,
    private val getCompleteShowUseCase: GetCompleteShowUseCase,
    private val getSeasonUseCase: GetSeasonUseCase,
    private val getEpisodeUseCase: GetEpisodeUseCase
) {
//    private val dataMapper by inject<DataMapper>()

    suspend fun execute(file: VideoFile) {
        repository.findShow(file.showName)?.let { show ->
            repository.findSeason(show.id, file.seasonNumber)?.let { season ->
                repository.findEpisode(season.id, file.seasonNumber)?.let { episodeId ->
                    // TODO What do we do ? Update file path ? Show duplicate ? Nothing
                } ?: insertNewEpisode(show, season.id, file)
            } ?: insertNewSeason(show, file)
        } ?: insertNewShow(file)
    }


    private suspend fun insertNewShow(videoFile: VideoFile) {
        searchShowUseCase.execute(videoFile.showName)
//            .next { /* TODO Check Data validity */ }
            .next { getCompleteShowUseCase.execute(it.id, videoFile) }
    }

    private suspend fun insertNewSeason(show: Show, videoFile: VideoFile) {
        getSeasonUseCase.execute(show.id, show.externalId?.tmdbId!!, videoFile)
    }

    private suspend fun insertNewEpisode(show: Show, seasonID: SeasonID, videoFile: VideoFile) {
        getEpisodeUseCase.execute(show.id, seasonID, show.externalId!!.tmdbId!!, videoFile)
    }
}
