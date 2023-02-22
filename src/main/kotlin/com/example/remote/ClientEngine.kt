package com.example.remote

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*

class ClientEngine {
    fun init(url: Url) = HttpClient(Apache) { setup(url) }
    fun intTest(url: Url, engine: HttpClientEngine) = HttpClient(engine) { setup(url) }
}