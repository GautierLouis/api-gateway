package com.example.usecases

import com.example.model.TMDBShowId
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason

class GetEpisodesBatchUseCase(
    private val service: TMDBService,
) {

    suspend fun execute(id: TMDBShowId, seasons: List<TMDBSeason>): Result<List<TMDBEpisode>> {
        return executeAlone(id, seasons)
            .map { list ->
                list.toList().map { it.episodes }.flatten()
            }
    }

    private var index = 0
    private val fetchSeason = mutableListOf<TMDBSeason>()

    private suspend fun executeAlone(id: TMDBShowId, seasons: List<TMDBSeason>): Result<MutableList<TMDBSeason>> {
        return try {
            val result = service.getSeason(id, seasons[index].seasonNumber)
            fetchSeason.add(result.getOrThrow())
            val isFinale = index.inc() == seasons.size
            if (isFinale) {
                Result.success(fetchSeason)
            } else {
                executeAlone(id, seasons)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}