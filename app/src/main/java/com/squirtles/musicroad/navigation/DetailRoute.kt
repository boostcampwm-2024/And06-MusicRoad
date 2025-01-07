package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface DetailRoute : Route {
    @Serializable
    data object Setting : DetailRoute
}
