package com.squirtles.musicroad.map.navigation

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.squirtles.musicroad.map.MapScreen
import com.squirtles.musicroad.map.MapViewModel
import com.squirtles.musicroad.media.PlayerServiceViewModel
import com.squirtles.musicroad.navigation.MapRoute
import com.squirtles.musicroad.navigation.Route
import com.squirtles.musicroad.detail.PickDetailScreen

fun NavController.navigateMap(navOptions: NavOptions? = null) {
    navigate(Route.Map, navOptions)
}

fun NavController.navigatePickDetail(pickId: String, navOptions: NavOptions? = null) {
    navigate(MapRoute.PickDetail(pickId), navOptions)
}

fun NavGraphBuilder.mapNavGraph(
    mapViewModel: MapViewModel,
    playerServiceViewModel: PlayerServiceViewModel,
    onFavoriteClick: (String) -> Unit,
    onCenterClick: () -> Unit,
    onUserInfoClick: (String) -> Unit,
    onPickSummaryClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onDeleted: (Context) -> Unit,
) {
    composable<Route.Map> {
        MapScreen(
            mapViewModel = mapViewModel,
            playerServiceViewModel = playerServiceViewModel,
            onFavoriteClick = onFavoriteClick,
            onCenterClick = onCenterClick,
            onUserInfoClick = onUserInfoClick,
            onPickSummaryClick = onPickSummaryClick,
        )
    }

    composable<MapRoute.PickDetail> { backStackEntry ->
        val pickId = backStackEntry.toRoute<MapRoute.PickDetail>().pickId

        PickDetailScreen(
            pickId = pickId,
            playerServiceViewModel = playerServiceViewModel,
            onUserInfoClick = onUserInfoClick,
            onBackClick = onBackClick,
            onDeleted = onDeleted,
        )
    }
}
