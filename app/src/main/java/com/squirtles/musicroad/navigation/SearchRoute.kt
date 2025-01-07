package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface SearchRoute : Route {
    @Serializable
    data object Create : SearchRoute
}

