package com.squirtles.musicroad.profile

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.squirtles.musicroad.ui.theme.White

data class MenuItem(
    val imageVector: ImageVector,
    val contentDescription: String,
    val iconColor: Color = White,
    val menuTitle: String,
    val onMenuClick: () -> Unit
)
