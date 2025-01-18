package com.squirtles.musicroad.map.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.ui.graphics.vector.ImageVector
import com.squirtles.musicroad.R
import com.squirtles.musicroad.map.BottomNavigationIconSize
import com.squirtles.musicroad.navigation.MapRoute

internal sealed class MapTab(
    @StringRes val contentDescription: Int,
    val icon: ImageVector,
    val iconSize: Int?,
    val route: MapRoute,
) {
    data class Favorite(val favoriteRoute: MapRoute.Favorite) : MapTab(
        contentDescription = R.string.map_navigation_favorite_icon_description,
        icon = Icons.Default.FavoriteBorder,
        iconSize = null,
        route = favoriteRoute
    )

    data class Profile(val profileRoute: MapRoute.Profile) : MapTab(
        contentDescription = R.string.map_navigation_setting_icon_description,
        icon = Icons.Outlined.AccountCircle,
        iconSize = null,
        route = profileRoute
    )

    data object Search : MapTab(
        contentDescription = R.string.map_navigation_center_icon_description,
        icon = Icons.Outlined.MusicNote,
        iconSize = BottomNavigationIconSize.CENTER_ICON.size,
        route = MapRoute.Search
    )
}
