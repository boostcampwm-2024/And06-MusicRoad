package com.squirtles.musicroad.navigation

import com.squirtles.domain.model.Song
import kotlinx.serialization.Serializable

@Serializable
sealed interface SearchRoute : Route {
    @Serializable
    data class Create(val song: Song) : SearchRoute
}
