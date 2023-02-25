package com.example.database

import com.example.model.*
import com.example.remote.tmdb.model.TMDBEpisode
import com.example.remote.tmdb.model.TMDBSeason
import com.example.remote.tmdb.model.TMDBShow

interface TMDBRepositoryInteraction {
    suspend fun findShow(cleanName: String): Show?
    suspend fun findSeason(id: ShowID, seasonNumber: Int): Season?
    suspend fun findEpisode(seasonID: SeasonID, episodeNumber: Int): Episode?
    suspend fun findEpisodes(showId: ShowID, seasonNumber: Int, episodeNumber: Int): Episode?


    suspend fun insertCompleteShow(tmdbShow: TMDBShow, cleanName: String): Result<Show>
    suspend fun updateEpisodePath(episodeID: EpisodeID, filePath: String)

    suspend fun insertSeason(showId: ShowID, tmdbSeason: TMDBSeason): Result<Season>
    suspend fun insertEpisodes(
        showId: ShowID,
        seasonID: SeasonID,
        tmdbEpisodes: List<TMDBEpisode>
    ): Result<List<Episode>>

    suspend fun insertEpisode(
        showId: ShowID,
        seasonID: SeasonID,
        episode: TMDBEpisode,
        filePath: String
    ): Result<Episode>

}

