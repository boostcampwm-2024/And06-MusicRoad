package com.squirtles.musicroad.search.navigation

import android.util.Log
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavController.navigateSearch(navOptions: NavOptions? = null) {
    navigate(MainRoute.Search, navOptions)
}

fun NavController.navigateCreate(song: Song, navOptions: NavOptions? = null) {
    val encodedSong = song.copy(
        previewUrl = "",
        externalUrl = "",
        genreNames = emptyList(),
        imageUrl = URLEncoder.encode(song.imageUrl, StandardCharsets.UTF_8.toString())
    )

    Log.d("SearchNavigation", "original: $song")
    Log.d("SearchNavigation", "encoded: $encodedSong")
    navigate(SearchRoute.Create(encodedSong), navOptions)
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
        typeMap = SearchRoute.Create.typeMap
    ) { backStackEntry ->
        val song = backStackEntry.toRoute<SearchRoute.Create>().song

        Log.d("SearchNavigation", "received: $song")

        CreatePickScreen(
            song = song,
            onBackClick = onBackClick,
            onCreateClick = onCreateClick,
        )
    }
}
