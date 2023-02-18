package com.example

import com.example.local.DatabaseFactory
import com.example.model.VideoFile
import com.example.plugins.*
import com.example.sync.DataMapper
import com.example.sync.Sync
import com.example.sync.mock
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.apache.http.util.Args


//fun main() {
//    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
//        .start(wait = true)
//}


fun main(args: Array<String>) = EngineMain.main(args)


fun Application.module() {
    DatabaseFactory.initInFile()
    configureKoin()
    configureHTTP()
    configureSerialization()
    configureSecurity()
    configureRouting()

    watchFiles()

//    DataMapper().checkDataValidity(mock)

}
