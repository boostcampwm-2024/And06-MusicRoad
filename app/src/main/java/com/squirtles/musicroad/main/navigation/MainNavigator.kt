package com.squirtles.musicroad.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.squirtles.domain.model.Song
import com.squirtles.musicroad.favorite.navigation.navigateFavorite
import com.squirtles.musicroad.map.navigation.navigateMap
import com.squirtles.musicroad.map.navigation.navigatePickDetail
import com.squirtles.musicroad.mypick.navigation.navigateMyPicks
import com.squirtles.musicroad.navigation.Route
import com.squirtles.musicroad.search.navigation.navigateCreate
import com.squirtles.musicroad.search.navigation.navigateSearch
import com.squirtles.musicroad.userinfo.navigation.navigateEditNotificationSetting
import com.squirtles.musicroad.userinfo.navigation.navigateEditProfile
import com.squirtles.musicroad.userinfo.navigation.navigateUserInfo

internal class MainNavigator(
    val navController: NavHostController
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val startDestination = Route.Map

    fun navigateMap() {
        navController.navigateMap(
            navOptions {
                popUpTo(startDestination) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        )
    }

    fun navigateFavorite(userId: String) {
        navController.navigateFavorite(
            userId = userId,
            navOptions {
                launchSingleTop = true
            }
        )
    }

    fun navigatePickDetail(pickId: String) {
        navController.navigatePickDetail(
            pickId = pickId,
            navOptions = navOptions {
                launchSingleTop = true
            }
        )
    }

    fun navigateMyPicks(userId: String) {
        navController.navigateMyPicks(
            userId = userId,
            navOptions = navOptions { launchSingleTop = true }
        )
    }

    fun navigateUserInfo(userId: String) {
        navController.navigateUserInfo(
            userId = userId,
            navOptions = navOptions { launchSingleTop = true }
        )
    }

    fun navigateEditProfile() {
        navController.navigateEditProfile(
            navOptions = navOptions { launchSingleTop = true }
        )
    }

    fun navigateEditNotificationSetting() {
        navController.navigateEditNotificationSetting(
            navOptions = navOptions { launchSingleTop = true }
        )
    }

    fun navigateSearch() {
        navController.navigateSearch(
            navOptions = navOptions { launchSingleTop = true }
        )
    }

    fun navigateCreate(song: Song) {
        navController.navigateCreate(
            song = song,
            navOptions = navOptions { launchSingleTop = true }
        )
    }

    private fun popBackStack() {
        navController.popBackStack()
    }

    fun popBackStackIfNotMap() {
        if (!isSameCurrentDestination<Route.Map>()) {
            popBackStack()
        }
    }

    private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
        return navController.currentDestination?.hasRoute<T>() == true
    }

}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}
