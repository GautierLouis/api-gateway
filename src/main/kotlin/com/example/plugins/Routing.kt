package com.example.plugins

import com.example.database.TMDBRepository
import com.example.file.FileWatcher
import com.example.model.VideoFile
import com.example.remote.tmdb.TMDBService
import com.example.remote.tvdb.TVDBService
import com.example.sync.SyncUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject
import java.io.File

fun Application.configureRouting() {
    val tvService by inject<TVDBService>()
    val tmService by inject<TMDBService>()
    val watcher: FileWatcher by inject { parametersOf(this.coroutineContext) }

    val repo by inject<TMDBRepository>()

    routing {

        get("/scan") {
            watcher.scan()
        }

        get("/file") {
            val file =
                File("/Users/louisgautier/Downloads/[Kaerizaki-Fansub] One Piece 1044 VOSTFR FHD (1920x1080).mp4")
            call.respondFile(file)
        }

        get("/") {
            val file =
                File("/Users/louisgautier/Documents/Plex/The.Boys.S03.MULTi.1080p.AMZN.WEBRip.DDP5.1.x265-R3MiX/The.Boys.S03E01.MULTi.1080p.AMZN.WEBRip.DDP5.1.x265-R3MiX.mkv")
            val videoFile = VideoFile("The Boys", 3, 1, file)
            SyncUseCase().triggerSync(videoFile)
        }
    }
}
