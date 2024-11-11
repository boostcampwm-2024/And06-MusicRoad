package com.example.spotifyapitest.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Results(
    @SerialName("songs") val songs: Songs? = null,
    @SerialName("music-videos") val musicVideos: MusicVideoResponse? = null
)
