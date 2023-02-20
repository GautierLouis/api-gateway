package com.example.usecases

import com.example.remote.tmdb.TMDBService
import com.example.remote.tmdb.model.TMDBSearchTV

class SearchShowUseCase(
    private val service: TMDBService
) {
    suspend fun execute(query: String): Result<TMDBSearchTV> {
        return service.searchSingle(query)
    }
}