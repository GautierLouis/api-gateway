package com.example.remote.tvdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TVDBResponse<T>(
    @SerialName("status") val status: String?,
    @SerialName("data") val data: T?,
    @SerialName("message") val message: String? = null
) {
    fun isValid() = status == "success"
}