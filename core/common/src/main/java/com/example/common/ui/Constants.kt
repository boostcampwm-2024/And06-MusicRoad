package com.example.common.ui

import android.util.Size
import androidx.compose.ui.unit.dp
import com.example.common.ui.theme.Black
import com.example.common.ui.theme.Primary

object Constants {
    val DEFAULT_PADDING = 16.dp

    val REQUEST_IMAGE_SIZE_DEFAULT = Size(300, 300)

    val COLOR_STOPS = arrayOf(
        0.0f to Primary,
        0.25f to Black
    )
}
