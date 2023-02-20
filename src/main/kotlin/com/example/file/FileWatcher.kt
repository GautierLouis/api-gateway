package com.example.file

import com.example.sync.SyncUseCase
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

class FileWatcher(
    override val coroutineContext: CoroutineContext,
    private val syncUseCase: SyncUseCase
) : CoroutineScope {

    private val dir = File("/Users/louisgautier/Desktop/TEst") // TODO Must be configurable
    private val watchChannel = dir.asWatchChannel(KWatchChannel.Mode.Recursive)
    private val patternMatcher = PatternMatcher()

    fun startWatching() {
        launch(Dispatchers.IO) {
            watchChannel.consumeEach { kWatchEvent: KWatchEvent ->
                val file = kWatchEvent.file
                if (file.isVideoFile() && kWatchEvent.kind == KWatchEvent.Kind.Created) {
                    syncFile(file)
                }
            }
        }
    }

    // Scan all files
    fun scan() {
        launch(Dispatchers.IO) {
            Files.walkFileTree(dir.toPath(), object : SimpleFileVisitor<Path>() {
                override fun preVisitDirectory(subPath: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val file = subPath.toFile()
                    if (file.isVideoFile()) {
                        launch(Dispatchers.IO) {
                            syncFile(file)
                        }
                    }
                    return FileVisitResult.CONTINUE
                }
            })
        }
    }

    private suspend fun syncFile(file: File) {
        val videoFile = patternMatcher.clean(file)
        syncUseCase.execute(videoFile)
    }
}



