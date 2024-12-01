package com.squirtles.musicroad.videoplayer

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import com.squirtles.domain.model.Pick

@OptIn(UnstableApi::class)
@Composable
fun MusicVideoScreen(
    pick: Pick,
    modifier: Modifier,
    onBackClick: () -> Unit,
    videoPlayerViewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val isLoading by videoPlayerViewModel.isLoading.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        MusicVideoPlayer(pick)
        VideoPlayerOverlay(pick, onBackClick)

        if (isLoading) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}
