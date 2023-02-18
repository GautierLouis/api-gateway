package com.example.remote.tvdb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TVDBLogin(
    @SerialName("token") val token: String
)

@Serializable
data class TVDBPostLogin(
    @SerialName("apikey") val key: String,
    @SerialName("pin") val pin: String
)