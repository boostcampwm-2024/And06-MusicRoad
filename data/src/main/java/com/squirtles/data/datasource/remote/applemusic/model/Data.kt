package com.squirtles.data.datasource.remote.applemusic.model

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val id: String,
    val attributes: Attributes,
)
