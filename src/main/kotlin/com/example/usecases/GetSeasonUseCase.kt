package com.example.usecases

import com.example.database.TMDBRepository
import com.example.model.Season
import com.example.model.ShowID
import com.example.model.TMDBShowId
import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBSeason
import com.example.utils.next

class SyncSeasonUseCase(
    private val getSeasonUseCase: GetSeasonUseCase,
    private val saveSeasonUseCase: SaveSeasonUseCase
) {

    suspend fun execute(showID: ShowID, tmdbId: TMDBShowId, seasonNumber: Int): Result<Season> {
        return getSeasonUseCase.execute(tmdbId, seasonNumber).next { saveSeasonUseCase.execute(showID, it) }
    }
}

class GetSeasonUseCase(
    private val service: TMDBService
) {
    suspend fun execute(id: TMDBShowId, seasonNumber: Int): Result<TMDBSeason> {
        return service.getSeason(id, seasonNumber)
    }
}


class SaveSeasonUseCase(
    private val repository: TMDBRepository
) {

    suspend fun execute(showID: ShowID, tmdbSeason: TMDBSeason): Result<Season> {
        return repository.insertSeason(showID, tmdbSeason)
    }
}