package com.example.file

import com.example.model.VideoFile
import java.io.File

class PatternMatcher {
    private val seasonInfoRegex = "S\\d\\dE\\d\\d".toRegex()
    private val vostRegex = "VOSTFR.*".toRegex()
    private val multiRegex = "MULTI.*".toRegex()
    private val bracketRegex = "\\[[^\\]]*\\]".toRegex()

    private fun String.containsSeasonInfo(): Boolean =
        this.contains(seasonInfoRegex)

    private fun String.cleanFileName(): String {
        return replace("S\\d\\dE\\d\\d.*".toRegex(), "")
            .replace(vostRegex, "")
            .replace(multiRegex, "")
            .replace(".", " ")
            .replace(bracketRegex, "")
    }

    private fun String.getSeasonNumber(): Int? {
        if (!containsSeasonInfo()) return null
        return try {
            seasonInfoRegex.find(this)
                ?.groupValues
                ?.firstOrNull()
                ?.removeRange(2, 4)
                ?.replace("S", "")
                ?.toInt()
        } catch (e: NumberFormatException) { null }
    }

    private fun String.getEpisodeNumber(): Int? {
        if (!containsSeasonInfo()) return null
        return try {
            seasonInfoRegex.find(this)
                ?.groupValues
                ?.firstOrNull()
                ?.removeRange(0, 4)
                ?.toInt()
        } catch (e: NumberFormatException) { null }
    }

    // TODO Handle edges cases
    fun clean(file: File): VideoFile {
        val name = file.nameWithoutExtension
        val seasonNumber = name.getSeasonNumber() ?: 0
        val episodeNumber = name.getEpisodeNumber() ?: 0
        val cleanName = name.cleanFileName()
        return VideoFile(cleanName, seasonNumber, episodeNumber, file)
    }
}