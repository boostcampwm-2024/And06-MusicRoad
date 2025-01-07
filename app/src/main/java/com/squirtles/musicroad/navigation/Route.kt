package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object Auth : Route
}
