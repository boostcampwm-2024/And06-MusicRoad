package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

sealed interface MainRoute : Route {
    @Serializable
    data object Search : MainRoute

    @Serializable
    data class Favorite(val userId: String) : MainRoute

    @Serializable
    data class Profile(val userId: String) : MainRoute
}
