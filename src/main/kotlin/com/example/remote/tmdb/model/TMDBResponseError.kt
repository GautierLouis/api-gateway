package com.example.remote.tmdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TMDBResponseError(
    @SerialName("status_message") val message: String,
    @SerialName("status_code") val code: Int,
    @SerialName("success") val success: Boolean
) {
    fun toThrowable() = Throwable("$message $code")
}