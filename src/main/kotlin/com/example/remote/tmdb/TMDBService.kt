package com.example.remote.tmdb

import com.example.remote.tmdb.model.TMDBSearchResult
import com.example.remote.tmdb.model.TMDBSearchTV
import com.example.remote.tmdb.model.TMDBShow
import com.example.remote.tmdb.model.TMDBShowExternalIds
import com.example.remote.tvdb.model.EpisodeElement
import com.example.remote.tvdb.model.Season
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

class TMDBService(token: String) : TMDBKtorService("https://api.themoviedb.org/3", token) {

    suspend fun search(query: String): Result<TMDBSearchTV> = call<TMDBSearchResult> {
        authClient.get("/search/tv?query=${query.encodeURLParameter()}")
    }.runCatching { this.getOrThrow().result.first() }

    suspend fun getShow(id: Int): Result<TMDBShow> = call {
        authClient.get("/tv/$id&language=${Locale.getDefault().language}")
    }

    suspend fun getExternalId(id: Long): Result<TMDBShowExternalIds> = call {
        authClient.get("/tv/$id/external_ids")
    }

    suspend fun getSeason(showId: String, seasonNumber: Int): Result<Season> = call {
        authClient.get("/tv/$showId/season/$seasonNumber")
    }

    suspend fun getEpisode(showId: String, seasonNumber: Int, episodeNumber: Int): Result<EpisodeElement> = call {
        authClient.get("/tv/$showId/season/$seasonNumber/episode/$episodeNumber")
    }

    suspend fun getAllSeasonsAndEpisodes(showId: String) {
        val groupId = authClient.get("/tv/$showId/episode_groups")
        authClient.get("/tv/episode_group/$groupId")
    }
}

