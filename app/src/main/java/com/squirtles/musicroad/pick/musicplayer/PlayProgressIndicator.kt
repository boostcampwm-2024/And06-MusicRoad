package com.squirtles.musicroad.pick.musicplayer

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.squirtles.musicroad.ui.theme.DarkGray
import com.squirtles.musicroad.ui.theme.White

@Composable
fun PlayProgressIndicator(
    modifier: Modifier = Modifier,
    currentTime: () -> Float,
    bufferPercentage: () -> Float,
    duration: Float,
    onSeekChanged: (Float) -> Unit
) {
    Box(modifier = modifier) {

        LinearProgressIndicator(
            progress = { bufferPercentage() / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = DarkGray,
            trackColor = Color.DarkGray,
            gapSize = 0.dp,
            drawStopIndicator = { }
        )

        LinearProgressIndicator(
            progress = { currentTime() / duration },
            modifier = Modifier.fillMaxWidth(),
            color = White,
            trackColor = Color.Transparent,
            gapSize = 0.dp,
            drawStopIndicator = { }
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(3.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val newTime = offset.x / size.width * duration
                        onSeekChanged(newTime)
                    }
                }
        )
    }
}
