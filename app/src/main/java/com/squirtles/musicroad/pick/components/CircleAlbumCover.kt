package com.squirtles.musicroad.pick.components

import android.util.Size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.squirtles.domain.model.Song
import com.squirtles.musicroad.R
import com.squirtles.musicroad.pick.components.music.visualizer.BaseVisualizer
import com.squirtles.musicroad.pick.components.music.visualizer.CircleVisualizer

@Composable
internal fun CircleAlbumCover(
    song: Song,
    currentPosition: () -> Long,
    duration: () -> Long,
    audioEffectColor: Color,
    baseVisualizer: () -> BaseVisualizer,
    audioSessionId: Int,
    onSeekChanged: (Long) -> Unit,
    onCenterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        CircleVisualizer(
            baseVisualizer = baseVisualizer,
            audioSessionId = audioSessionId,
            color = audioEffectColor,
            sizeRatio = 0.5f,
            modifier = Modifier.align(Alignment.Center)
        )

        PlayCircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .align(Alignment.Center)
                .zIndex(1f),
            currentTime = currentPosition().toFloat(),
            duration = duration().toFloat(),
            strokeWidth = 5.dp,
            onSeekChanged = { timeMs ->
                onSeekChanged(timeMs.toLong())
            },
            onCenterClick = onCenterClick,
        )

        AsyncImage(
            model = song.getImageUrlWithSize(Size(400, 400)),
            contentDescription = song.albumName + stringResource(id = R.string.pick_album_description),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(Alignment.Center)
                .zIndex(0f),
            contentScale = ContentScale.Crop
        )
    }
}
