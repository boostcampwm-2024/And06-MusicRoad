package com.squirtles.musicroad.search.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.squirtles.domain.model.Song
import com.squirtles.musicroad.create.CreatePickScreen
import com.squirtles.musicroad.navigation.MainRoute
import com.squirtles.musicroad.navigation.SearchRoute
import com.squirtles.musicroad.search.SearchMusicScreen
import com.squirtles.musicroad.utils.serializableType
import kotlin.reflect.typeOf

fun NavController.navigateSearch(navOptions: NavOptions? = null) {
    navigate(MainRoute.Search, navOptions)
}

fun NavController.navigateCreate(song: Song,  navOptions: NavOptions? = null) {
    navigate(SearchRoute.Create(song), navOptions)
}

fun NavGraphBuilder.searchNavGraph(
    onBackClick: () -> Unit,
    onItemClick: (Song) -> Unit,
    onCreateClick: (String) -> Unit
) {
    composable<MainRoute.Search> {
        SearchMusicScreen(
            onBackClick = onBackClick,
            onItemClick = onItemClick, // Create 이동
        )
    }
    composable<SearchRoute.Create>(
        typeMap = mapOf(typeOf<Song>() to serializableType<Song>())
    ) { backStackEntry ->
        val song = backStackEntry.toRoute<SearchRoute.Create>().song

        CreatePickScreen(
            song = song,
            onBackClick = onBackClick,
            onCreateClick = onCreateClick,
        )
    }
}
