package com.example.remote.tvdb

import com.example.remote.AnonymousKtor
import com.example.remote.tvdb.model.TVDBResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class TVDBKtorService(baseUrl: String) {

    abstract suspend fun getToken(): BearerTokens?
    abstract suspend fun getRefreshToken(): BearerTokens?

    private val baseClient = AnonymousKtor.init(Url(baseUrl))

    val anonClient = baseClient
    val authClient = baseClient.config {
        install(Auth) {
            bearer {
                loadTokens { getToken() }
                refreshTokens { getRefreshToken() }
            }
        }
    }

    /**
     * Define a base method to call an Endpoint. Will catch non 200 code and return the serialised result
     */
    internal suspend inline fun <reified T> call(endpoint: () -> HttpResponse): Result<T> {
        return try {
            val response: HttpResponse = endpoint()
            val result = response.body<TVDBResponse<T>>()

            when {
                response.status != HttpStatusCode.OK -> {
                    Result.failure(Exception(response.status.value.toString()))
                }

                !result.isValid() -> {
                    Result.failure(Exception(result.message))
                }

                result.data == null -> {
                    Result.failure(Exception("No data for"))
                }

                else -> Result.success(result.data)
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}