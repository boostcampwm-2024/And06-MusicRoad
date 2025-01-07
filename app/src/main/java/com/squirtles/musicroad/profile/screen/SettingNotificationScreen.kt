package com.squirtles.musicroad.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Text
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.Constants.COLOR_STOPS
import com.squirtles.musicroad.common.Constants.DEFAULT_PADDING
import com.squirtles.musicroad.common.DefaultTopAppBar
import com.squirtles.musicroad.ui.theme.White

@Composable
internal fun SettingNotificationScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(id = R.string.setting_notification_title),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops = COLOR_STOPS))
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(id = R.string.setting_notification_description),
                modifier = Modifier
                    .padding(horizontal = DEFAULT_PADDING)
                    .align(Alignment.Center),
                color = White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
