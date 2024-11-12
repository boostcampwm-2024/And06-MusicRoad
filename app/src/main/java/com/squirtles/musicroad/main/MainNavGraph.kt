package com.squirtles.musicroad.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.squirtles.musicroad.favorite.FavoriteScreen
import com.squirtles.musicroad.map.MapScreen
import com.squirtles.musicroad.map.MapViewModel
import com.squirtles.musicroad.search.SearchMusicScreen
import com.squirtles.musicroad.setting.SettingScreen

@Composable
fun MainNavGraph(
    mapViewModel: MapViewModel,
    navController: NavHostController,
    navigationActions: MainNavigationActions,
    modifier: Modifier = Modifier,
    startDestination: String = MainDestinations.MAIN_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(MainDestinations.MAIN_ROUTE) {
            MapScreen(
                mapViewModel = mapViewModel,
                onFavoriteClick = navigationActions.navigateToFavorite,
                onCenterClick = { navController.navigate(CreatePickDestinations.CREATE_ROUTE) },
                onSettingClick = navigationActions.navigateToSetting
            )
        }

        composable(MainDestinations.FAVORITE_ROUTE) {
            FavoriteScreen()
        }

        composable(MainDestinations.SETTING_ROUTE) {
            SettingScreen()
        }

        navigation(
            startDestination = CreatePickDestinations.SEARCH_MUSIC_ROUTE,
            route = CreatePickDestinations.CREATE_ROUTE
        ) {
            composable(CreatePickDestinations.SEARCH_MUSIC_ROUTE) {
                SearchMusicScreen()
            }
            composable(CreatePickDestinations.CREATE_PICK_ROUTE) {

            }
        }
    }
}
