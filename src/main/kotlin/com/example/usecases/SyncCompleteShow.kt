package com.example.usecases

import com.example.database.TMDBRepository
import com.example.model.Show
import com.example.model.TMDBShowId
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.ShowEpisodeGroupID
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBShow
import com.example.utils.next


class SyncCompleteShow(
    private val searchShowUseCase: SearchShowUseCase,
    private val getCompleteShowUseCase: GetCompleteShowUseCase,
    private val saveCompleteShowUseCase: SaveCompleteShowUseCase,
) {

    suspend fun execute(showName: String): Result<Show> {
        return searchShowUseCase.execute(showName).next {
            getCompleteShowUseCase.execute(it.id)
        }.next {
            saveCompleteShowUseCase.execute(it, showName)
                .onSuccess {
                    //TODO Update Episode file path
                }
        }
    }
}


class GetCompleteShowUseCase(
    private val service: TMDBService,
    private val getEpisodesBatchUseCase: GetEpisodesBatchUseCase
) {
    suspend fun execute(id: TMDBShowId): Result<TMDBShow> {
        return try {
            val remoteShow = service.getShow(id).getOrThrow()
            val externalIds = service.getShowExternalId(id).getOrThrow()

            //TODO Try to make this call only if group exist
            val episodeGroup = service.getEpisodeGroupId(id).getOrThrow()

            val remoteEpisodes = getEpisodes(episodeGroup, remoteShow).getOrThrow().filter { it.seasonNumber > 0 }

            val seasonWithEpisodes = remoteShow.realSeasons.map { tmdbSeason ->
                val episodeInSeason = remoteEpisodes.filter { it.seasonNumber == tmdbSeason.seasonNumber }
                tmdbSeason.copy(episodes = episodeInSeason)
            }

            Result.success(remoteShow.copy(externalIds = externalIds, seasons = seasonWithEpisodes))

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun getEpisodes(
        episodeGroup: List<ShowEpisodeGroupID>,
        show: TMDBShow
    ): Result<List<TMDBEpisode>> {
        return if (episodeGroup.isEmpty()) {
            getEpisodesBatchUseCase.execute(show.tmdbId, show.realSeasons)
        } else if (episodeGroup.size == 1) {
            // Best scenario - can take a lot of time. Ex: 35s for One piece (~ 1050 episodes)
            service.getEpisodeGroup(episodeGroup.first().groupId)
        } else {
            //TODO What if it has multiple groups ?
            Result.failure(Exception("Multiple group found"))
        }
    }
}

class SaveCompleteShowUseCase(
    private val repository: TMDBRepository
) {

    suspend fun execute(tmdbShow: TMDBShow, showName: String): Result<Show> {
        return repository.insertCompleteShow(tmdbShow, showName)
    }
}

