package com.example.model

import java.io.File

data class VideoFile(
    val showName: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val file: File
)