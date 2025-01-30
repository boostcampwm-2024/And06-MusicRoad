package com.squirtles.musicroad.userinfo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.squirtles.musicroad.navigation.MainRoute
import com.squirtles.musicroad.navigation.UserInfoRoute
import com.squirtles.musicroad.userinfo.screen.EditNotificationSettingScreen
import com.squirtles.musicroad.userinfo.screen.EditProfileScreen
import com.squirtles.musicroad.userinfo.screen.UserInfoScreen

fun NavController.navigateUserInfo(userId: String, navOptions: NavOptions? = null) {
    navigate(MainRoute.UserInfo(userId), navOptions)
}

fun NavController.navigateEditProfile(navOptions: NavOptions? = null) {
    navigate(UserInfoRoute.EditProfile, navOptions)
}

fun NavController.navigateEditNotificationSetting(navOptions: NavOptions? = null) {
    navigate(UserInfoRoute.EditNotification, navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    onBackClick: () -> Unit,
    onBackToMapClick: () -> Unit,
    onFavoritePicksClick: (String) -> Unit,
    onMyPicksClick: (String) -> Unit,
    onSettingProfileClick: () -> Unit,
    onSettingNotificationClick: () -> Unit,
) {
    composable<MainRoute.UserInfo> { backStackEntry ->
        val userId = backStackEntry.toRoute<MainRoute.UserInfo>().userId

        UserInfoScreen(
            userId = userId,
            onBackClick = onBackClick,
            onBackToMapClick = onBackToMapClick,
            onFavoritePicksClick = onFavoritePicksClick,
            onMyPicksClick = onMyPicksClick,
            onSettingProfileClick = onSettingProfileClick,
            onSettingNotificationClick = onSettingNotificationClick,
        )
    }

    composable<UserInfoRoute.EditProfile> { backStackEntry ->
        EditProfileScreen(
            onBackClick = onBackClick,
        )
    }

    composable<UserInfoRoute.EditNotification> { backStackEntry ->
        EditNotificationSettingScreen(
            onBackClick = onBackClick
        )
    }
}
