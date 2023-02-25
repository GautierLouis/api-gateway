package com.example.file

import com.example.usecases.SyncUseCase
import com.example.utils.isVideoFile
import dev.vishna.watchservice.KWatchChannel
import dev.vishna.watchservice.KWatchEvent
import dev.vishna.watchservice.asWatchChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.io.File
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import kotlin.coroutines.CoroutineContext
import kotlin.streams.toList

class FileWatcher(
    override val coroutineContext: CoroutineContext,
    private val syncUseCase: SyncUseCase
) : CoroutineScope {

    private val dir = File("/volume1/TV Show") // TODO Must be configurable
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

    fun scanDebug(): List<VideoFileExport> {
        return Files.walk(dir.toPath()).filter { it.toFile().isVideoFile() }
            .toList()
            .mapNotNull { patternMatcher.clean(it.toFile()) }
    }

    private suspend fun syncFile(file: File) {
        val cleanFile = patternMatcher.clean(file)
        syncUseCase.execute(cleanFile)
    }
}

@Serializable
sealed class FileInfo {
    data class Default(val seasonNumber: Int, val episodeNumber: Int) : FileInfo()
    data class Anime(val episodeNumber: Int) : FileInfo()
    object Other : FileInfo()
}

@Serializable
data class VideoFileExport(
    val path: String,
    val originalName: String,
    val cleanName: String,
    val info: FileInfo,
) {
    val seasonNumber: Int? = when (info) {
        is FileInfo.Default -> info.seasonNumber
        else -> null
    }
    val episodeNumber: Int? = when (info) {
        is FileInfo.Anime -> info.episodeNumber
        is FileInfo.Default -> info.episodeNumber
        FileInfo.Other -> null
    }
}



