package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface MainRoute : Route {
    @Serializable
    data object Search : MainRoute

    @Serializable
    data class PickDetail(val pickId: String) : MainRoute

    @Serializable
    data object Profile : MainRoute
}
