package com.squirtles.musicroad.pick.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.squirtles.musicroad.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.math.atan2

@Composable
internal fun PlayCircularProgressIndicator(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    currentTime: Float,
    strokeWidth: Dp,
    duration: Float,
    innerRadiusRatio: Float = 0.5f, // 터치 비활성 비율
    onSeekChanged: (Float) -> Unit,
    onCenterClick: () -> Unit = {},
) {
    var showIcon by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val centerX = size.width / 2
                    val centerY = size.height / 2

//                    val distanceFromCenter = hypot(offset.x - centerX, offset.y - centerY) // 터치 좌표랑 중심 사이
//                    val innerRadius = size.width * innerRadiusRatio / 2
//                    if (distanceFromCenter < innerRadius) return@detectTapGestures // 중앙의 반경 내 터치 무시

                    // 중심 기준 터치좌표의 각도
                    val angle = Math
                        .toDegrees(atan2(offset.y - centerY, offset.x - centerX).toDouble())
                        .toFloat()

                    // 원의 위쪽 점(12시 방향)을 기준으로 시계 방향
                    val normalizedAngle = ((angle + 360) % 360 + 90) % 360

                    // 각도를 재생 시간으로 변환
                    val seekTime = (normalizedAngle / 360f) * duration
                    onSeekChanged(seekTime)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(30.dp)
//                .background(White)
                .clickable {
                    onCenterClick()
                    showIcon = true
                },
        )

        AnimatedVisibility(
            visible = showIcon,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
                .padding(30.dp)
        ) {
            Icon(
                imageVector = if (isPlaying()) Icons.Default.PlayArrow else Icons.Default.Pause,
                contentDescription = "Play/Pause",
                modifier = Modifier
                    .size(64.dp)
                    .alpha(0.7f),
                tint = Color.White
            )
        }
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            progress = { currentTime / duration },
            color = White,
            trackColor = Color.Transparent,
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round,
            gapSize = 0.dp,
        )

        if (showIcon) {
            LaunchedEffect(Unit) {
                delay(500) // 아이콘이 표시될 시간 (500ms)
                showIcon = false
            }
        }
    }
}

