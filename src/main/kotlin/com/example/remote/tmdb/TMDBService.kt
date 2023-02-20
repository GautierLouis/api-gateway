package com.example.remote.tmdb

import com.example.model.TMDBShowId
import com.example.remote.tmdb.model.*
import com.example.utils.next
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

//    suspend fun getSeason(showId: TMDBID, seasonNumber: Int): Result<Season> = call {
//        authClient.get("/tv/$showId/season/$seasonNumber")
//    }
//
//    suspend fun getEpisode(showId: TMDBID, seasonNumber: Int, episodeNumber: Int): Result<EpisodeElement> = call {
//        authClient.get("/tv/$showId/season/$seasonNumber/episode/$episodeNumber")
//    }

    suspend fun getAllSeasonsAndEpisodes(showId: TMDBShowId): Result<List<EpisodeGroup>> {
        return getEpisodeGroupId(showId).next { getEpisodeGroup(it.groupId) }
            .map { it.groups ?: emptyList() }
    }

    private suspend fun getEpisodeGroup(groupId: String): Result<ShowEpisodeGroupID> = call {
        authClient.get("/tv/episode_group/$groupId")
    }

    private suspend fun getEpisodeGroupId(showId: TMDBShowId): Result<ShowEpisodeGroupID> =
        call<ShowEpisodeGroupResponse> {
            authClient.get("/tv/$showId/episode_groups")
        }.runCatching { this.getOrThrow().result.first() }
}

