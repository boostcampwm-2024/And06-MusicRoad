package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface MapRoute : Route {
    @Serializable
    data object Search : MapRoute

    @Serializable
    data class PickDetail(val pickId: String) : MapRoute

    @Serializable
    data object Profile : MapRoute
}
