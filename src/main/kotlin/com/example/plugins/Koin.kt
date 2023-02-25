package com.example.plugins

import com.example.database.TMDBRepository
import com.example.database.TMDBRepositoryInteraction
import com.example.database.TokenDao
import com.example.database.TokenRepository
import com.example.file.FileWatcher
import com.example.plugins.SecretsEnv.TMDB_ENGINE
import com.example.plugins.SecretsEnv.TMDB_TOKEN
import com.example.plugins.SecretsEnv.TVDB_KEY
import com.example.plugins.SecretsEnv.TVDB_PIN
import com.example.remote.ClientEngine
import com.example.remote.tmdb.TMDBService
import com.example.remote.tvdb.TVDBService
import com.example.usecases.*
import io.ktor.http.*
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    val conf = this.environment.config
    install(Koin) {
        modules(module {
            single(named(TMDB_TOKEN)) { conf.property("ktor.secrets.tmdb_token").getString() }
            single(named(TVDB_KEY)) { conf.property("ktor.secrets.tvdb_key").getString() }
            single(named(TVDB_PIN)) { conf.property("ktor.secrets.tvdb_pin").getString() }

            single { TokenRepository() } bind TokenDao::class
            single { TMDBRepository() } bind TMDBRepositoryInteraction::class

            single { FileWatcher(get(), get()) }

            single(named(TMDB_ENGINE)) { ClientEngine().init(Url("https://api.themoviedb.org/3")) }

            single {
                TMDBService(
                    get(named(TMDB_TOKEN))
                )
            }
            single {
                TVDBService(
                    get(),
                    get(named(TVDB_KEY)),
                    get(named(TVDB_PIN)),
                )
            }

            single { SyncUseCase(get(), get(), get(), get()) }

            single { SearchShowUseCase(get()) }

            single { SyncCompleteShow(get(), get(), get()) }
            single { GetCompleteShowUseCase(get(), get()) }
            single { SaveCompleteShowUseCase(get()) }

            single { SyncSeasonUseCase(get(), get()) }
            single { GetSeasonUseCase(get()) }
            single { SaveSeasonUseCase(get()) }

            single { SyncEpisodeUseCase(get(), get()) }
            single { GetEpisodeUseCase(get()) }
            single { SaveEpisodeUseCase(get()) }

            single { GetEpisodesBatchUseCase(get()) }

        })
    }
}

object SecretsEnv {
    const val TMDB_TOKEN = "TMDB_TOKEN"
    const val TVDB_KEY = "TVDB_KEY"
    const val TVDB_PIN = "TVDB_PIN"

    const val TMDB_ENGINE = "TMDB_ENGINE"
}