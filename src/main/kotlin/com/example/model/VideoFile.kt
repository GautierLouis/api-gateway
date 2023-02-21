package com.example.model

import java.io.File

data class VideoFile(
    val showName: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val file: File
) {
    fun match(seasonNumber: Int, episodeNumber: Int) =
        this.seasonNumber == seasonNumber && this.episodeNumber == episodeNumber
}