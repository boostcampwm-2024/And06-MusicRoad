package com.example.spotifyapitest.data

import kotlinx.serialization.Serializable

@Serializable
data class Preview(
    val url: String,
    val hlsUrl: String? = null,
    val artwork: Artwork? = null,
)
