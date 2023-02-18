package com.example.utils

import java.io.File

fun File.isVideoFile(): Boolean {
    val types = listOf(
        "3g2",
        "3gp",
        "asf",
        "avi",
        "flv",
        "m4v",
        "mkv",
        "mov",
        "mp4",
        "mpg",
        "mpeg",
        "rm",
        "swf",
        "vob",
        "wmv",
    )
    return this.extension.lowercase() in types
}