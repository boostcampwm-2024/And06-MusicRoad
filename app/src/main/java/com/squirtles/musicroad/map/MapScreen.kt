package com.squirtles.musicroad.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.squirtles.musicroad.map.components.BottomNavigation
import com.squirtles.musicroad.map.components.ClusterBottomSheet
import com.squirtles.musicroad.map.components.InfoWindow
import com.squirtles.musicroad.map.components.PickNotificationBanner
import com.squirtles.musicroad.musicplayer.PlayerViewModel

@Composable
fun MapScreen(
    mapViewModel: MapViewModel,
    onFavoriteClick: () -> Unit,
    onCenterClick: () -> Unit,
    onSettingClick: () -> Unit,
    onPickSummaryClick: (String) -> Unit,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {
    val nearPicks by mapViewModel.nearPicks.collectAsStateWithLifecycle()
    val lastLocation by mapViewModel.lastLocation.collectAsStateWithLifecycle()

    val clickedMarkerState by mapViewModel.clickedMarkerState.collectAsStateWithLifecycle()
    val playerState by playerViewModel.playerState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var showBottomSheet by remember { mutableStateOf(false) }
    var isPlaying: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(nearPicks) {
        if (nearPicks.isNotEmpty()) {
            playerViewModel.readyPlayerSetList(context, nearPicks.map { it.song.previewUrl })
        }
    }

    LaunchedEffect(playerState) {
        isPlaying = playerState.isPlaying
    }

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NaverMap(
                mapViewModel = mapViewModel,
                lastLocation = lastLocation
            )

            if (nearPicks.isNotEmpty()) {
                PickNotificationBanner(
                    nearPicks = nearPicks,
                    isPlaying = isPlaying,
                    onClick = {
                        playerViewModel.shuffleNextItem()
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                clickedMarkerState.prevClickedMarker?.let {
                    if (clickedMarkerState.curPickId != null) { // 단말 마커 클릭 시
                        showBottomSheet = false
                        mapViewModel.picks[clickedMarkerState.curPickId]?.let { pick ->
                            InfoWindow(
                                pick,
                                navigateToPick = { pickId ->
                                    playerViewModel.pause()
                                    onPickSummaryClick(pickId)
                                },
                                calculateDistance = { lat, lng ->
                                    mapViewModel.calculateDistance(lat, lng).let { distance ->
                                        when {
                                            distance >= 1000.0 -> "%.1fkm".format(distance / 1000.0)
                                            distance >= 0 -> "%.0fm".format(distance)
                                            else -> ""
                                        }
                                    }
                                }
                            )
                        }
                    } else { // 클러스터 마커 클릭 시
                        showBottomSheet = true
                    }
                }

                Spacer(Modifier.height(16.dp))

                BottomNavigation(
                    modifier = Modifier.padding(bottom = 16.dp),
                    lastLocation = lastLocation,
                    onFavoriteClick = {
                        playerViewModel.pause()
                        onFavoriteClick()
                    },
                    onCenterClick = {
                        playerViewModel.pause()
                        onCenterClick()
                        mapViewModel.saveCurLocationForced()
                    },
                    onSettingClick = {
                        playerViewModel.pause()
                        onSettingClick()
                    }
                )
            }

            if (showBottomSheet) {
                ClusterBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                        mapViewModel.resetClickedMarkerState(context)
                    },
                    clusterPickList = clickedMarkerState.clusterPickList,
                    calculateDistance = { lat, lng ->
                        mapViewModel.calculateDistance(lat, lng).let { distance ->
                            when {
                                distance >= 1000.0 -> "%.1fkm".format(distance / 1000.0)
                                distance >= 0 -> "%.0fm".format(distance)
                                else -> ""
                            }
                        }

                    },
                    onClickItem = { pickId ->
                        playerViewModel.pause()
                        onPickSummaryClick(pickId)
                    }
                )
            }
        }
    }
}
