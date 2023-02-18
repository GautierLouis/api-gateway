package com.example.remote

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val jsonBuilder = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    allowStructuredMapKeys = true
    explicitNulls = false
    encodeDefaults = true

}

object AnonymousKtor {
    fun init(baseUrl: Url): HttpClient {
        return HttpClient(Apache) {
            install(ContentNegotiation) {
                json(jsonBuilder)
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 5)
                exponentialDelay()
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            defaultRequest {
                url {
                    protocol = baseUrl.protocol
                    host = baseUrl.host + baseUrl.fullPath
                }
            }
        }
    }
}