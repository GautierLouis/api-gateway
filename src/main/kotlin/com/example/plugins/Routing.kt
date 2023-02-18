package com.example.plugins

import com.example.remote.tmdb.TMDBService
import com.example.remote.tvdb.TVDBService
import com.example.file.FileWatcher
import com.example.local.ShowRepository
import com.example.model.VideoFile
import com.example.sync.mock
import io.ktor.http.*
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

    val repo by inject<ShowRepository>()

    routing {

        get("/scan") {
            watcher.scan()
        }

        get("/file") {
            val file =
                File("/Users/louisgautier/Downloads/[Kaerizaki-Fansub] One Piece 1044 VOSTFR FHD (1920x1080).mp4")
            call.respondFile(file)
        }

//        get("/") {
//            val file =
//                File("/Users/louisgautier/Documents/Plex/Obi.Wan.Kenobi.S01.MULTi.1080p.WEB.H264-FW/Obi.Wan.Kenobi.S01E01.MULTi.1080p.WEB.H264-FW.mkv")
//            val videoFile = VideoFile("Obi Wan Kenobi", 1, 1, file)
//            repo.insertShow(mock, videoFile)
//        }

        get("/") {

        }
    }
}
