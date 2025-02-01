package com.squirtles.musicroad.main.navigations

import androidx.navigation.NavHostController

class MainNavigationActions(navController: NavHostController) {
    val navigateToMain: () -> Unit = {
        navController.navigate(MainDestinations.MAIN_ROUTE) {
            popUpTo(route = MainDestinations.MAIN_ROUTE) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    val navigateToFavoritePicks: (String) -> Unit = { userId ->
        navController.navigate(ProfileDestination.favoritePicks(userId)) {
            launchSingleTop = true
        }
    }

    val navigateToMyPicks: (String) -> Unit = { userId ->
        navController.navigate(ProfileDestination.myPicks(userId)) {
            launchSingleTop = true
        }
    }

    val navigateToProfile: (String?) -> Unit = { userId ->
        navController.navigate(ProfileDestination.profile(userId)) {
            launchSingleTop = true
        }
    }

    val navigateToSearch: () -> Unit = {
        navController.navigate(CreatePickDestinations.SEARCH_MUSIC_ROUTE) {
            launchSingleTop = true
        }
    }

    val navigateToCreate: () -> Unit = {
        navController.navigate(CreatePickDestinations.CREATE_PICK_ROUTE) {
            launchSingleTop = true
        }
    }

    val navigateToPickDetail: (String) -> Unit = { pickId ->
        navController.navigate(PickInfoDestinations.pickDetail(pickId)) {
            launchSingleTop = true
        }
    }
}
