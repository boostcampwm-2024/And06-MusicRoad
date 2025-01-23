package com.squirtles.musicroad.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.squirtles.musicroad.navigation.MainRoute
import com.squirtles.musicroad.navigation.ProfileRoute
import com.squirtles.musicroad.profile.screen.ProfileScreen
import com.squirtles.musicroad.profile.screen.SettingNotificationScreen
import com.squirtles.musicroad.profile.screen.SettingProfileScreen

fun NavController.navigateProfile(userId: String, navOptions: NavOptions? = null) {
    navigate(MainRoute.Profile(userId), navOptions)
}

fun NavController.navigateSettingProfile(navOptions: NavOptions? = null) {
    navigate(ProfileRoute.Setting, navOptions)
}

fun NavController.navigateSettingNotification(navOptions: NavOptions? = null) {
    navigate(ProfileRoute.Notification, navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    onBackClick: () -> Unit,
    onBackToMapClick: () -> Unit,
    onFavoritePicksClick: (String) -> Unit,
    onMyPicksClick: (String) -> Unit,
    onSettingProfileClick: () -> Unit,
    onSettingNotificationClick: () -> Unit,
) {
    composable<MainRoute.Profile> { backStackEntry ->
        val userId = backStackEntry.toRoute<MainRoute.Profile>().userId

        ProfileScreen(
            userId = userId,
            onBackClick = onBackClick,
            onBackToMapClick = onBackToMapClick,
            onFavoritePicksClick = onFavoritePicksClick,
            onMyPicksClick = onMyPicksClick,
            onSettingProfileClick = onSettingProfileClick,
            onSettingNotificationClick = onSettingNotificationClick,
        )
    }

    composable<ProfileRoute.Setting> { backStackEntry ->
        SettingProfileScreen(
            onBackClick = onBackClick,
        )
    }

    composable<ProfileRoute.Notification> { backStackEntry ->
        SettingNotificationScreen(
            onBackClick = onBackClick
        )
    }
}
