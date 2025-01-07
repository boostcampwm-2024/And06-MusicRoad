package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface ProfileRoute : Route {
    @Serializable
    data object Setting : ProfileRoute
}

