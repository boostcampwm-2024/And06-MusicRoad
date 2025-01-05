package com.squirtles.musicroad.detail.components.music.visualizer

import android.media.audiofx.Visualizer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.math.sqrt

class BaseVisualizer {
    private var visualizer: Visualizer? = null

    private val _fftFlow = MutableSharedFlow<List<Float>>(replay = 1)
    val fftFlow: SharedFlow<List<Float>> = _fftFlow.asSharedFlow()

    private val validRange = getSignificantFftIndexRange()

    fun setVisualizer(audioSessionId: Int) {
        val visualizer = Visualizer(audioSessionId)
        this.visualizer = visualizer
    }

    fun setVisualizerListener() {
        visualizer?.run {
            enabled = false
            captureSize = CAPTURE_SIZE
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(visualizer: Visualizer, bytes: ByteArray, samplingRate: Int) {
                    // NOT USED
                }

                override fun onFftDataCapture(visualizer: Visualizer, bytes: ByteArray, samplingRate: Int) {
                    // bytes = [실수부,허수부,실수부,허수부 ....]
                    val size = bytes.size / 4 // 각 복소수당 2바이트
                    val magnitudes = FloatArray(size)

                    for (i in 0 until size) {
                        val real = bytes[2 * i].toInt()
                        val imaginary = bytes[2 * i + 1].toInt()
                        magnitudes[i] = sqrt((real * real + imaginary * imaginary).toDouble()).toFloat()
                    }

                    val filteredMagnitudes = magnitudes.copyOfRange(validRange.first, validRange.last + 1)
                    _fftFlow.tryEmit(filteredMagnitudes.toList())
                }
            }, Visualizer.getMaxCaptureRate() / 2, false, true)
            enabled = true
        }
    }

    fun release() {
        visualizer?.release()
        visualizer = null
    }

    // 20 ~ 4000 Hz 사이만 필터링
    private fun getSignificantFftIndexRange(
        samplingRate: Int = SAMPLING_RATE,
        captureSize: Int = CAPTURE_SIZE,
        minFreq: Int = MIN_FREQ,
        maxFreq: Int = MAX_FREQ
    ): IntRange {
        val resolution = samplingRate.toDouble() / captureSize
        val startIndex = (minFreq / resolution).toInt()
        val endIndex = (maxFreq / resolution).toInt()
        return startIndex..endIndex
    }

    companion object {
        const val CAPTURE_SIZE = 512
        const val SAMPLING_RATE = 22000
        const val MIN_FREQ = 20
        const val MAX_FREQ = 4500
    }
}
