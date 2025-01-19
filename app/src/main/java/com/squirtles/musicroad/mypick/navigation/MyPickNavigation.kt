package com.squirtles.musicroad.mypick.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.squirtles.musicroad.mypick.MyPickScreen
import com.squirtles.musicroad.navigation.ProfileRoute

fun NavController.navigateMyPicks(userId: String, navOptions: NavOptions) {
    navigate(ProfileRoute.MyPicks(userId), navOptions)
}

fun NavGraphBuilder.myPickNavGraph(
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    composable<ProfileRoute.MyPicks> { backStackEntry ->
        val userId = backStackEntry.toRoute<ProfileRoute.MyPicks>().userId

        MyPickScreen(
            userId = userId,
            onBackClick = onBackClick,
            onItemClick = onItemClick
        )
    }
}
