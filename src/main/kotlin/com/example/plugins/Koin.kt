package com.example.plugins

import com.example.database.ShowDao
import com.example.database.ShowRepository
import com.example.database.TokenDao
import com.example.database.TokenRepository
import com.example.file.FileWatcher
import com.example.plugins.SecretsEnv.TMDB_TOKEN
import com.example.plugins.SecretsEnv.TVDB_KEY
import com.example.plugins.SecretsEnv.TVDB_PIN
import com.example.remote.tmdb.TMDBService
import com.example.remote.tvdb.TVDBService
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    val conf = this.environment.config
    install(Koin) {
        modules(module {
            single { TokenRepository() } bind TokenDao::class

            single { FileWatcher(get()) }

            single { ShowRepository() } bind ShowDao::class

            single(named(TMDB_TOKEN)) { conf.property("ktor.secrets.tmdb_token").toString() }
            single(named(TVDB_KEY)) { conf.property("ktor.secrets.tvdb_key").toString() }
            single(named(TVDB_PIN)) { conf.property("ktor.secrets.tvdb_pin").toString() }

            single { TMDBService(get(named(TMDB_TOKEN))) }
            single { TVDBService(get(), get(named(TVDB_KEY)), get(named(TVDB_PIN))) }


        })
    }
}

object SecretsEnv {
    const val TMDB_TOKEN = "TMDB_TOKEN"
    const val TVDB_KEY = "TVDB_KEY"
    const val TVDB_PIN = "TVDB_PIN"
}