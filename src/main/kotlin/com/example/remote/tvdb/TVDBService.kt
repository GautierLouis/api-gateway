package com.example.remote.tvdb

import com.example.model.Token
import com.example.local.TokenRepository
import com.example.remote.tvdb.model.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*

class TVDBService(
    private val tokenRepository: TokenRepository,
    private val tvdbKey: String,
    private val tvdbPin: String,
) : TVDBKtorService("https://api4.thetvdb.com/v4") {

    override suspend fun getToken(): BearerTokens? {
        val localToken = tokenRepository.loadToken("tvdb")?.value

        return if (localToken == null) {
            val token = login().getOrNull()?.token ?: return null
            tokenRepository.saveToken(Token("tvdb", token))
            BearerTokens(token, "")
        } else {
            BearerTokens(localToken, "")
        }
    }

    override suspend fun getRefreshToken(): BearerTokens? {
        val result = login()
        return result.getOrNull()?.let {
            tokenRepository.saveToken(Token("tvdb", it.token))
            BearerTokens(it.token, "")
        }
    }

    private suspend fun login(): Result<TVDBLogin> = call {
        anonClient.post("/login") {
            contentType(ContentType.Application.Json)
            setBody(TVDBPostLogin(tvdbKey, tvdbPin))
        }
    }

    suspend fun search(query: String): Result<List<TVDBSearch>> = call {
        authClient.get("/search?query=$query")
    }

    suspend fun getShow(id: Int): Result<ShowExtended> = call {
        authClient.get("/series/$id/extended?meta=episodes&short=false")
    }

    suspend fun getSeason(id: Int): Result<Season> = call {
        authClient.get("/seasons/$id/extended")
    }

    suspend fun getEpisode(id: Int): Result<EpisodeElement> = call {
        authClient.get("/episodes/$id/extended")
    }

}

