package com.squirtles.musicroad.map.marker

import androidx.compose.ui.graphics.toArgb
import com.squirtles.musicroad.ui.theme.Primary20
import com.squirtles.musicroad.ui.theme.Primary50
import com.squirtles.musicroad.ui.theme.Primary80

enum class DensityType(val offset: Int, val color: Int) {
    LOW(4, Primary80.toArgb()),
    MEDIUM(2, Primary50.toArgb()),
    HIGH(0, Primary20.toArgb())
}
