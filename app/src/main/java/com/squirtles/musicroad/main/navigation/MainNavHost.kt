package com.squirtles.musicroad.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.squirtles.musicroad.favorite.navigation.favoriteNavGraph
import com.squirtles.musicroad.map.MapViewModel
import com.squirtles.musicroad.map.navigation.mapNavGraph
import com.squirtles.musicroad.media.PlayerServiceViewModel
import com.squirtles.musicroad.mypick.navigation.myPickNavGraph
import com.squirtles.musicroad.search.navigation.searchNavGraph
import com.squirtles.musicroad.userinfo.navigation.profileNavGraph

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navigator: MainNavigator,
    mapViewModel: MapViewModel = hiltViewModel(),
    playerServiceViewModel: PlayerServiceViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navigator.navController,
        startDestination = navigator.startDestination,
    ) {
        mapNavGraph(
            mapViewModel = mapViewModel,
            playerServiceViewModel = playerServiceViewModel,
            onFavoriteClick = navigator::navigateFavorite,
            onCenterClick = navigator::navigateSearch,
            onProfileClick = navigator::navigateUserInfo,
            onPickSummaryClick = navigator::navigatePickDetail,
            onBackClick = navigator::navigateMap,
            onDeleted = mapViewModel::resetClickedMarkerState
        )

        searchNavGraph(
            onBackClick = navigator::popBackStackIfNotMap,
            onItemClick = navigator::navigateCreate,
            onCreateClick = navigator::navigatePickDetail,
        )

        favoriteNavGraph(
            onBackClick = navigator::popBackStackIfNotMap,
            onItemClick = navigator::navigatePickDetail
        )

        profileNavGraph(
            onBackClick = navigator::popBackStackIfNotMap,
            onBackToMapClick = navigator::navigateMap,
            onFavoritePicksClick = navigator::navigateFavorite,
            onMyPicksClick = navigator::navigateMyPicks,
            onSettingProfileClick = navigator::navigateEditProfile,
            onSettingNotificationClick = navigator::navigateEditNotificationSetting,
        )

        myPickNavGraph(
            onBackClick = navigator::popBackStackIfNotMap,
            onItemClick = navigator::navigatePickDetail
        )
    }
}
