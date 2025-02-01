package com.squirtles.musicroad.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface UserInfoRoute : Route {
    @Serializable
    data class MyPicks(val userId: String) : UserInfoRoute

    @Serializable
    data object EditProfile : UserInfoRoute

    @Serializable
    data object EditNotification : UserInfoRoute
}

