package com.example.plugins

import com.example.database.MDBRepositoryInteraction
import com.example.database.TMDBRepository
import com.example.database.TokenDao
import com.example.database.TokenRepository
import com.example.file.FileWatcher
import com.example.plugins.SecretsEnv.TMDB_TOKEN
import com.example.plugins.SecretsEnv.TVDB_KEY
import com.example.plugins.SecretsEnv.TVDB_PIN
import com.example.remote.tmdb.TMDBService
import com.example.remote.tvdb.TVDBService
import com.example.usecases.GetCompleteShowUseCase
import com.example.usecases.GetEpisodesBatchUseCase
import com.example.usecases.SearchShowUseCase
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

            single { TMDBRepository() } bind MDBRepositoryInteraction::class

            single(named(TMDB_TOKEN)) { conf.property("ktor.secrets.tmdb_token").getString() }
            single(named(TVDB_KEY)) { conf.property("ktor.secrets.tvdb_key").getString() }
            single(named(TVDB_PIN)) { conf.property("ktor.secrets.tvdb_pin").getString() }

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


            single { SearchShowUseCase(get()) }
            single { GetCompleteShowUseCase(get(), get(), get()) }
            single { GetEpisodesBatchUseCase(get()) }
        })
    }
}

object SecretsEnv {
    const val TMDB_TOKEN = "TMDB_TOKEN"
    const val TVDB_KEY = "TVDB_KEY"
    const val TVDB_PIN = "TVDB_PIN"
}