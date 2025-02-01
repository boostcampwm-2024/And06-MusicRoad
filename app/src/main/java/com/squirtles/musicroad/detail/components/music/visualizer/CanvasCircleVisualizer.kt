package com.squirtles.musicroad.detail.components.music.visualizer

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

@Composable
internal fun CanvasCircle(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val radius = max(width, height) * 0.4f

        drawCircle(
            color = color,
            radius = radius,
            style = Stroke(width = 4f)
        )
    }
}

/* Bar 형태 원형 시각화 */
@Composable
internal fun CanvasSoundEffectBar(
    audioData: List<Float>,
    color: Color,
    sizeRatio: Float,
    modifier: Modifier = Modifier
) {

    val offsetAngle = Math.toRadians(-90.0)
    val angleStep = 360f / audioData.size
    val points = mutableListOf<Offset>()

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val radius = max(width, height) * sizeRatio

        audioData.forEachIndexed { i, magnitude ->
            // 현재 오디오데이터가 그려질 각도
            val angle = Math.toRadians((i * angleStep).toDouble()) + offsetAngle
            val cosValue = cos(angle)
            val sinValue = sin(angle)
            val barHeight = magnitude * height / 5

            // 현재 오디오데이터가 그려질 xy 좌표
            val startX = (width / 2 + radius * cosValue).toFloat()
            val startY = (height / 2 + radius * sinValue).toFloat()
            val endX = (width / 2 + (radius + barHeight) * cosValue).toFloat()
            val endY = (height / 2 + (radius + barHeight) * sinValue).toFloat()

            points.add(Offset(startX, startY))
            points.add(Offset(endX, endY))
        }

        drawPoints(
            points = points,
            pointMode = PointMode.Lines,
            color = color,
            strokeWidth = 24f
        )
    }
}

/* Wave 형태 원형 시각화 */
@Composable
internal fun CanvasSoundEffectWave(
    audioData: List<Float>,
    color: Color,
    modifier: Modifier = Modifier
) {

    val offsetAngle = Math.toRadians(-90.0)
    val angleStep = 360f / audioData.size
    val path = Path()

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val radius = max(width, height) * 0.45f

        audioData.forEachIndexed { i, magnitude ->
            val angle = Math.toRadians((i * angleStep).toDouble()) + offsetAngle
            val cosValue = cos(angle)
            val sinValue = sin(angle)
            val barHeight = magnitude * height / 5

            val startX = (width / 2 + radius * cosValue).toFloat()
            val startY = (height / 2 + radius * sinValue).toFloat()
            val endX = (width / 2 + (radius + barHeight) * cosValue).toFloat()
            val endY = (height / 2 + (radius + barHeight) * sinValue).toFloat()

            if (i == 0) {
                path.moveTo(startX, startY)
            } else {

                path.lineTo(endX, endY)
            }
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 4f)
        )
    }
}

private const val STROKE_WIDTH = 18f
