package com.squirtles.musicroad.main.navigations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.squirtles.musicroad.create.CreatePickScreen
import com.squirtles.musicroad.create.CreatePickViewModel
import com.squirtles.musicroad.create.SearchMusicScreen
import com.squirtles.musicroad.map.MapScreen
import com.squirtles.musicroad.map.MapViewModel
import com.squirtles.musicroad.media.PlayerServiceViewModel
import com.squirtles.musicroad.pick.DetailPickScreen
import com.squirtles.musicroad.picklist.PickListScreen
import com.squirtles.musicroad.picklist.PickListType
import com.squirtles.musicroad.profile.ProfileScreen
import com.squirtles.musicroad.setting.SettingNotificationScreen
import com.squirtles.musicroad.setting.SettingProfileScreen

@Composable
fun MainNavGraph(
    modifier: Modifier = Modifier,
    mapViewModel: MapViewModel = hiltViewModel(),
    playerServiceViewModel: PlayerServiceViewModel = hiltViewModel(),
    navController: NavHostController,
    navigationActions: MainNavigationActions,
) {
    NavHost(
        navController = navController,
        startDestination = MainDestinations.MAIN_ROUTE,
        modifier = modifier
    ) {
        composable(MainDestinations.MAIN_ROUTE) {
            MapScreen(
                mapViewModel = mapViewModel,
                playerServiceViewModel = playerServiceViewModel,
                onFavoriteClick = { userId -> navigationActions.navigateToFavoritePicks(userId) },
                onCenterClick = navigationActions.navigateToSearch,
                onUserInfoClick = { userId -> navigationActions.navigateToProfile(userId) },
                onPickSummaryClick = { pickId -> navigationActions.navigateToPickDetail(pickId) },
            )
        }

        composable(
            route = ProfileDestination.favoritePicks("{userId}"),
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            PickListScreen(
                userId = userId,
                pickListType = PickListType.FAVORITE,
                onBackClick = { navController.navigateUp() },
                onItemClick = { pickId -> navigationActions.navigateToPickDetail(pickId) }
            )
        }

        composable(
            route = ProfileDestination.myPicks("{userId}"),
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            PickListScreen(
                userId = userId,
                pickListType = PickListType.CREATED,
                onBackClick = { navController.navigateUp() },
                onItemClick = { pickId -> navigationActions.navigateToPickDetail(pickId) }
            )
        }

        composable(
            route = ProfileDestination.profile("{userId}"),
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            ProfileScreen(
                userId = userId,
                onBackClick = { navController.navigateUp() },
                onBackToMapClick = navigationActions.navigateToMain,
                onFavoritePicksClick = { navigationActions.navigateToFavoritePicks(it) },
                onMyPicksClick = { navigationActions.navigateToMyPicks(it) },
                onSettingProfileClick = { navController.navigate(ProfileDestination.SETTING_PROFILE_ROUTE) },
                onSettingNotificationClick = { navController.navigate(ProfileDestination.SETTING_NOTIFICATION_ROUTE) },
            )
        }

        composable(ProfileDestination.SETTING_PROFILE_ROUTE) {
            SettingProfileScreen(onBackClick = { navController.navigateUp() })
        }

        composable(ProfileDestination.SETTING_NOTIFICATION_ROUTE) {
            SettingNotificationScreen(onBackClick = { navController.navigateUp() })
        }

        navigation(
            startDestination = CreatePickDestinations.SEARCH_MUSIC_ROUTE,
            route = CreatePickDestinations.CREATE_ROUTE
        ) {
            composable(CreatePickDestinations.SEARCH_MUSIC_ROUTE) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(CreatePickDestinations.CREATE_ROUTE)
                }
                SearchMusicScreen(
                    createPickViewModel = hiltViewModel<CreatePickViewModel>(parentEntry),
                    onBackClick = { navController.navigateUp() },
                    onItemClick = navigationActions.navigateToCreate
                )
            }

            composable(CreatePickDestinations.CREATE_PICK_ROUTE) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry(CreatePickDestinations.CREATE_ROUTE)
                }
                CreatePickScreen(
                    createPickViewModel = hiltViewModel<CreatePickViewModel>(parentEntry),
                    onBackClick = { navController.navigateUp() },
                    onCreateClick = { pickId -> navigationActions.navigateToPickDetail(pickId) }
                )
            }
        }

        composable(
            route = PickInfoDestinations.pickDetail("{pickId}"),
            arguments = listOf(navArgument("pickId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pickId = backStackEntry.arguments?.getString("pickId") ?: ""

            DetailPickScreen(
                pickId = pickId,
                playerServiceViewModel = playerServiceViewModel,
                onProfileClick = { userId -> navigationActions.navigateToProfile(userId) },
                onBackClick = { // 픽  등록에서 정보 화면으로 간 것이라면 뒤로 가기 시 메인으로, 아니라면 이전 화면으로
                    if (navController.previousBackStackEntry?.destination?.route == CreatePickDestinations.CREATE_PICK_ROUTE) {
                        navigationActions.navigateToMain()
                    } else {
                        navController.navigateUp()
                    }
                },
                onDeleted = { mapViewModel.resetClickedMarkerState(it) },
            )
        }
    }
}
