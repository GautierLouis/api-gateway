package com.example.plugins

import com.example.file.FileWatcher
import io.ktor.server.application.*
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject

fun Application.watchFiles() {
    val watcher: FileWatcher by inject { parametersOf(this.coroutineContext) }
    watcher.startWatching()
}
