package com.example.file

import com.example.sync.Sync
import com.example.utils.isVideoFile
import dev.vishna.watchservice.KWatchChannel
import dev.vishna.watchservice.KWatchEvent
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.coroutines.CoroutineContext

class FileWatcher(override val coroutineContext: CoroutineContext) : CoroutineScope {

    private val dir = File("/Users/louisgautier/Desktop/TEst")
    private val watchChannel = dir.asWatchChannel(KWatchChannel.Mode.Recursive)
    private val patternMatcher = PatternMatcher()
    private val sync = Sync()

    fun startWatching() {
        launch {
            watchChannel.consumeEach { kWatchEvent: KWatchEvent ->
                val file = kWatchEvent.file
                if (file.isVideoFile() && kWatchEvent.kind == KWatchEvent.Kind.Created) {
                    syncFile(file)
                }
            }
        }
    }

    fun scan() {
        launch(Dispatchers.IO) {
            Files.walkFileTree(dir.toPath(), object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(subPath: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val file = subPath.toFile()
                    if (file.isVideoFile()) {
                        launch(Dispatchers.IO) {
                            val videoFile = patternMatcher.clean(file)
                            sync.triggerSync(videoFile)
                        }
                    }
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    private suspend fun syncFile(file: File) {
        val videoFile = patternMatcher.clean(file)
        sync.triggerSync(videoFile)
    }
}



