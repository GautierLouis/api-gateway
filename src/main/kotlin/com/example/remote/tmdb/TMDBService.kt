package com.example.remote.tmdb

import com.example.model.TMDBShowId
import com.example.remote.tmdb.model.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.*

class TMDBService(token: String) : TMDBKtorService(token) {

    suspend fun searchSingle(query: String): Result<TMDBSearchTV> =
        search(query).mapCatching { it.result.first() }

    suspend fun search(query: String) = call<TMDBSearchResult> {
        authClient.get("/search/tv?query=${query.encodeURLParameter()}")
    }

    suspend fun getShow(id: TMDBShowId): Result<TMDBShow> = call {
        authClient.get("/tv/$id&language=${Locale.getDefault().language}")
    }

    suspend fun getShowExternalId(id: TMDBShowId): Result<TMDBShowExternalIds> = call {
        authClient.get("/tv/$id/external_ids")
    }

    suspend fun getSeason(showId: TMDBShowId, seasonNumber: Int): Result<TMDBSeason> = call {
        authClient.get("/tv/$showId/season/$seasonNumber")
    }

    suspend fun getEpisode(showId: TMDBShowId, seasonNumber: Int, episodeNumber: Int): Result<TMDBEpisode> = call {
        authClient.get("/tv/$showId/season/$seasonNumber/episode/$episodeNumber")
    }

    suspend fun getEpisodeGroup(groupId: String): Result<List<TMDBEpisode>> = call<ShowEpisodeGroupID> {
        authClient.get("/tv/episode_group/$groupId")
    }.runCatching { this.getOrThrow().groups!!.flatMap { it.episodes } }

    // TODO Avoid this call = Find a way to know is a show has his episodes grouped or not
    suspend fun getEpisodeGroupId(showId: TMDBShowId): Result<List<ShowEpisodeGroupID>> =
        call<ShowEpisodeGroupResponse> {
            authClient.get("/tv/$showId/episode_groups")
        }.runCatching { getOrThrow().result }
}

