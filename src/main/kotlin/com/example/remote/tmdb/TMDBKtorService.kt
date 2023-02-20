package com.example.remote.tmdb

import com.example.remote.setup
import com.example.remote.tmdb.model.TMDBResponseError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.statement.*
import io.ktor.http.*

abstract class TMDBKtorService(
    token: String
) {

    private val url = Url("https://api.themoviedb.org/3")

    private val baseClient = HttpClient(Apache) { setup(url) }

    val authClient = baseClient.config {
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(token, "")
                }
            }
        }
    }

    /**
     * Define a base method to call an Endpoint. Will catch non 200 code and return the serialised result
     */
    internal suspend inline fun <reified T> call(endpoint: () -> HttpResponse): Result<T> {
        return try {
            val response: HttpResponse = endpoint()

            when {
                response.status != HttpStatusCode.OK -> {
                    val body = response.body<TMDBResponseError>()
                    Result.failure(body.toThrowable())
                }

                else -> {
                    Result.success(response.body())
                }
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}