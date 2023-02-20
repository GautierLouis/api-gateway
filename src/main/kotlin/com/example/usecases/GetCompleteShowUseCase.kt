package com.example.usecases

import com.example.database.TMDBRepository
import com.example.model.TMDBShowId
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.ShowEpisodeGroupID
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow


// TODO Split into 2 uses case : 1 requesting, 1 inserting ?
class GetCompleteShowUseCase(
    private val service: TMDBService,
    private val repository: TMDBRepository,
    private val getEpisodesBatchUseCase: GetEpisodesBatchUseCase
) {

    suspend fun execute(id: TMDBShowId, videoFile: VideoFile): Result<Unit> {
        return try {
            val show = service.getShow(id).getOrThrow()
            val externalIds = service.getShowExternalId(id).getOrThrow()

            //TODO Try to make this call only if group exist
            val episodeGroup = service.getEpisodeGroupId(id).getOrThrow()

            val episodes = getEpisodes(episodeGroup, show).getOrThrow()

            repository.insertShow(show, videoFile)
            repository.insertExternalIds(externalIds)
            repository.insertEpisodes(episodes, videoFile)

            Result.success(Unit) // Should I return something ?

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getEpisodes(
        episodeGroup: List<ShowEpisodeGroupID>,
        show: TMDBShow
    ): Result<List<TMDBEpisode>> {
        return if (episodeGroup.isEmpty()) {
            getEpisodesBatchUseCase.execute(show.tmdbId, show.seasons)
        } else if (episodeGroup.size == 1) {
            // Best scenario - can take a lot of time. Ex: 35s for One piece (~ 1050 episodes)
            service.getEpisodeGroup(episodeGroup.first().groupId)
        } else {
            //TODO What if it has multiple groups ?
            Result.success(emptyList())
        }
    }
}

