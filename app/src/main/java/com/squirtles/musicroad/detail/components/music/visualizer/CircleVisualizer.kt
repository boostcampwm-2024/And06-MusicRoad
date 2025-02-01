package com.squirtles.musicroad.detail.components.music.visualizer

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.squirtles.musicroad.ui.theme.White
import kotlinx.coroutines.launch

@Composable
fun CircleVisualizer(
    baseVisualizer: () -> BaseVisualizer,
    audioSessionId: Int,
    color: Color = White,
    sizeRatio: Float,
    modifier: Modifier = Modifier
) {
    val magnitudes = remember { mutableStateOf<List<Animatable<Float, AnimationVector1D>>>(emptyList()) }
    val visualizer = baseVisualizer()

    LaunchedEffect(Unit) {
        visualizer.setVisualizer(audioSessionId)
        visualizer.setVisualizerListener()
        visualizer.fftFlow.collect { fftArray ->
            val normalizedData = processAudioData(fftArray)

            if (magnitudes.value.isEmpty()) {
                magnitudes.value = normalizedData.map { Animatable(it) }
            } else {
                normalizedData.forEachIndexed { i, magnitude ->
                    launch {
                        magnitudes.value[i].animateTo(
                            targetValue = magnitude,
                            animationSpec = tween(
                                durationMillis = 120,
                                easing = FastOutSlowInEasing
                            )
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            visualizer.release()
        }
    }

    CanvasSoundEffectBar(
        audioData = magnitudes.value.map { it.value },
        color = color,
        sizeRatio = sizeRatio,
        modifier = modifier
    )
}

private fun processAudioData(audioData: List<Float>): List<Float> {
    val scaledData = scaleAudioData(audioData)
    return normalizeAudioData(scaledData)
}

/* 주파수 대역별 가중치  */
private fun scaleAudioData(audioData: List<Float>): List<Float> {
    val size = audioData.size
    return audioData.mapIndexed { index, value ->
        val scaleFactor = when {
            index < size / 8 -> 0.4f
            index < size / 4 -> 1.0f // 저주파 대역
            index < size / 2 -> 2.0f // 중간 대역
            index < size / 1.33 -> 3.0f // 고주파 대역
            else -> 5.0f // 고주파 대역
        }
        value * scaleFactor
    }
}

private fun applyLogScale(audioData: List<Float>): List<Float> {
    val epsilon = 1e-6f // 0 방지용 작은 값
    val minValue = audioData.minOrNull() ?: 0f
    val offset = if (minValue < 1f) 1f - minValue else 0f // 최소값을 1로 이동

    return audioData.map { value ->
        val shiftedValue = value + offset + epsilon // 데이터를 양수 범위로 이동
        20 * kotlin.math.log10(shiftedValue) // 로그 변환
    }
}

/* 다이나믹 레인지 압축 */
private fun compressDynamicRangeRoot(audioData: List<Float>): List<Float> {
    return audioData.map { kotlin.math.sqrt(it) }
}

/* 0.0 ~ 1.0 사이 정규화 */
private fun normalizeAudioData(audioData: List<Float>): List<Float> {
    val max = audioData.maxOrNull() ?: 1f // 데이터 최대값
    val min = audioData.minOrNull() ?: 0f // 데이터 최소값
    return if (max - min <= 1f) List(audioData.size) { 0f } else audioData.map { (it - min) / (max - min) }
}

