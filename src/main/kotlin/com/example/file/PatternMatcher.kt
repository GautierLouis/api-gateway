package com.example.file

import java.io.File

class PatternMatcher {

    private val defaultPattern = "S\\d{1,2}E\\d{1,2}".toRegex()
    private val oldPattern = "\\d{1,2}x\\d{1,2}".toRegex()
    private val animePattern =
        "([1-9][0-9][0-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9]|[0-9])".toRegex() // Could match 1080, 720

    private val defaultSeasonPattern = "S\\d{1,2}".toRegex()
    private val defaultEpisodePattern = "E\\d{1,2}".toRegex()
    private val oldEpisodePattern = "Ep.\\d{1,2}".toRegex()

    private val twoDigitPattern = "\\d{1,2}".toRegex()

    //Noise
    private val vostRegex = "VOSTFR.*".toRegex()
    private val multiRegex = "MULTi.*".toRegex()
    private val frenchRegex = "French.*".toRegex()
    private val frenchCapsRegex = "FRENCH.*".toRegex()
    private val bracketRegex = "\\[[^\\]]*\\]".toRegex()
    private val parenthesesRegex = "\\([^\\]]*\\)".toRegex()

    private fun String.cleanFileName(): String {
        return replace("S\\d{1,2}E\\d{1,2}.*".toRegex(), "")
            .replace("\\d{1,2}x\\d{1,2}.*".toRegex(), "")
            .replace("\\d{3,4}.*".toRegex(), "")
            .replace("([1-9][0-9][0-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9]|[0-9]).*".toRegex(), "")
            .replace("Ep.\\d{1,2}.*".toRegex(), "")
            .trim()
    }

    private fun String.removeNoise(): String {
        return replace(vostRegex, "")
            .replace(multiRegex, "")
            .replace(frenchRegex, "")
            .replace(frenchCapsRegex, "")
            .replace(bracketRegex, "")
            .replace(parenthesesRegex, "")
            .replace("-", " ")
            .replace(".", " ")
            .trim()
    }

    private fun String.extractSeasonNumber(): Int? {
        return if (contains(defaultSeasonPattern)) {
            defaultSeasonPattern.find(this)!!.groupValues.first().toInt()
        } else if (contains(oldPattern)) {
            twoDigitPattern.find(this)!!.groupValues.first().toInt()
        } else null
    }

    private fun String.extractEpisodeNumber(): Int? {
        return if (contains(defaultEpisodePattern)) {
            defaultEpisodePattern.find(this)!!.groupValues[1].toInt()
        } else if (contains(oldPattern)) {
            twoDigitPattern.find(this)!!.groupValues[1].toInt()
        } else if (contains(oldEpisodePattern)) {
            oldEpisodePattern.find(this)!!.groupValues.first().toInt()
        } else if (contains(animePattern)) {
            animePattern.find(this)!!.groupValues.first().toInt()
        } else null
    }

    private fun String.extractInfos(): FileInfo {
        val seasonNumber = extractSeasonNumber()
        val episodeNumber = extractEpisodeNumber()

        return if (seasonNumber != null && episodeNumber != null) {
            FileInfo.Default(seasonNumber, episodeNumber)
        } else if (episodeNumber != null) {
            FileInfo.Anime(episodeNumber)
        } else {
            FileInfo.Other
        }
    }

    // TODO Handle edges cases
    fun clean(file: File): VideoFileExport {
        val name = file.nameWithoutExtension
        val noiseless = name.removeNoise()
        val info = noiseless.extractInfos()
        val cleanName = noiseless.cleanFileName()

        return VideoFileExport(
            file.absolutePath,
            name,
            cleanName,
            info
        )
    }
}