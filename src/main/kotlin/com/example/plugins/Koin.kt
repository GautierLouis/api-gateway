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
import io.ktor.server.config.*
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun envModule(conf: ApplicationConfig) = module {
    single(named(TMDB_TOKEN)) { conf.property("ktor.secrets.tmdb_token").getString() }
    single(named(TVDB_KEY)) { conf.property("ktor.secrets.tvdb_key").getString() }
    single(named(TVDB_PIN)) { conf.property("ktor.secrets.tvdb_pin").getString() }
}

val engineModule = module {
    // Must be keep out for test
    single(named(TMDB_ENGINE)) { ClientEngine().init(Url("https://api.themoviedb.org/3")) }
}

val serviceModule = module {
    // Network layer
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
}

val repositoryModule = module {
    // Data layer
    single { TokenRepository() } bind TokenDao::class
    single { TMDBRepository() } bind TMDBRepositoryInteraction::class
}

val useCaseModules = module {
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
}

val koinModules = listOf(
    serviceModule,
    repositoryModule,
    useCaseModules,
    module {
        // Feature that doesn't fit
        single { FileWatcher(get(), get()) }
    }
)

fun Application.configureKoin() {
    install(Koin) {
        module {
            envModule(this@configureKoin.environment.config)
            engineModule
        }
        modules(koinModules)
    }
}

object SecretsEnv {
    const val TMDB_TOKEN = "TMDB_TOKEN"
    const val TVDB_KEY = "TVDB_KEY"
    const val TVDB_PIN = "TVDB_PIN"

    const val TMDB_ENGINE = "TMDB_ENGINE"
}