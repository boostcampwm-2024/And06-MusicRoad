package com.squirtles.musicroad.pick

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.squirtles.domain.model.Pick
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.AlertStringDialog
import com.squirtles.musicroad.common.DialogTextButton
import com.squirtles.musicroad.common.HorizontalSpacer
import com.squirtles.musicroad.common.VerticalSpacer
import com.squirtles.musicroad.media.PlayerServiceViewModel
import com.squirtles.musicroad.pick.PickViewModel.Companion.DEFAULT_PICK
import com.squirtles.musicroad.pick.components.CircleAlbumCover
import com.squirtles.musicroad.pick.components.CommentText
import com.squirtles.musicroad.pick.components.DetailPickTopAppBar
import com.squirtles.musicroad.pick.components.MusicVideoKnob
import com.squirtles.musicroad.pick.components.PickInformation
import com.squirtles.musicroad.pick.components.SongInfo
import com.squirtles.musicroad.pick.components.music.MusicPlayer
import com.squirtles.musicroad.pick.components.music.visualizer.BaseVisualizer
import com.squirtles.musicroad.ui.theme.Black
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White
import com.squirtles.musicroad.videoplayer.MusicVideoScreen
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun DetailPickScreen(
    pickId: String,
    pickViewModel: PickViewModel = hiltViewModel(),
    playerServiceViewModel: PlayerServiceViewModel,
    onProfileClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onDeleted: (Context) -> Unit,
) {
    val context = LocalContext.current
    val uiState by pickViewModel.detailPickUiState.collectAsStateWithLifecycle()
    var showDeletePickDialog by rememberSaveable { mutableStateOf(false) }
    var showProcessIndicator by rememberSaveable { mutableStateOf(false) }
    var isMusicVideoAvailable by remember { mutableStateOf(false) }

    BackHandler {
        if (showProcessIndicator.not()) {
            onBackClick()
        }
    }

    LaunchedEffect(Unit) {
        pickViewModel.fetchPick(pickId)
    }

    when (uiState) {
        DetailPickUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is DetailPickUiState.Success -> {
            val lifecycleOwner = LocalLifecycleOwner.current
            val pick = (uiState as DetailPickUiState.Success).pick
            val isFavorite = (uiState as DetailPickUiState.Success).isFavorite
            val isCreatedBySelf = pickViewModel.getUserId() == pick.createdBy.userId
            var favoriteCount by rememberSaveable { mutableIntStateOf(pick.favoriteCount) }
            val onActionClick: () -> Unit = {
                when {
                    isCreatedBySelf -> {
                        playerServiceViewModel.onPause()
                        showDeletePickDialog = true
                    }

                    isFavorite -> {
                        showProcessIndicator = true
                        pickViewModel.toggleFavoritePick(
                            pickId = pickId,
                            isAdding = false
                        )
                    }

                    else -> {
                        showProcessIndicator = true
                        pickViewModel.toggleFavoritePick(
                            pickId = pickId,
                            isAdding = true
                        )
                    }
                }
            }

            val scrollScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
                pageCount = { if (isMusicVideoAvailable) 2 else 1 }
            )

            LaunchedEffect(Unit) {
                pickViewModel.favoriteAction
                    .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                    .collect { action ->
                        when (action) {
                            FavoriteAction.ADDED -> {
                                showProcessIndicator = false
                                favoriteCount += 1
                                context.showShortToast(context.getString(R.string.success_add_to_favorite))
                            }

                            FavoriteAction.DELETED -> {
                                showProcessIndicator = false
                                favoriteCount -= 1
                                context.showShortToast(context.getString(R.string.success_delete_at_favorite))
                            }
                        }
                    }
            }

            // 비디오 플레이어 설정
            LaunchedEffect(pick) {
                playerServiceViewModel.readyPlayer()
                playerServiceViewModel.setMediaItem(pick)
                isMusicVideoAvailable = pick.musicVideoUrl.isNotEmpty()
            }

            LaunchedEffect(pagerState) {
                pagerState.scrollToPage(page = pickViewModel.currentTab)
            }

            DisposableEffect(Unit) {
                onDispose {
                    pickViewModel.setCurrentTab(pagerState.currentPage)
                }
            }

            HorizontalPager(
                state = pagerState
            ) { page ->
                when (page) {
                    DETAIL_PICK_TAB -> {
                        DetailPick(
                            pick = pick,
                            isCreatedBySelf = isCreatedBySelf,
                            isFavorite = isFavorite,
                            userId = pick.createdBy.userId,
                            userName = pick.createdBy.userName,
                            favoriteCount = favoriteCount,
                            isMusicVideoAvailable = isMusicVideoAvailable,
                            onProfileClick = onProfileClick,
                            playerServiceViewModel = playerServiceViewModel,
                            onBackClick = {
                                onBackClick()
                            },
                            onActionClick = onActionClick,
                        )
                    }

                    MUSIC_VIDEO_TAB -> {
                        MusicVideoScreen(
                            pick = pick,
                            modifier = Modifier
                                .background(Black)
                                .graphicsLayer {
                                    val pageOffset = (
                                            (pagerState.currentPage - page) + pagerState
                                                .currentPageOffsetFraction
                                            ).absoluteValue
                                    alpha = lerp(
                                        start = 0.5f,
                                        stop = 1f,
                                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    )
                                },
                            onBackClick = {
                                scrollScope.launch {
                                    pagerState.animateScrollToPage(page = DETAIL_PICK_TAB)
                                }
                            },
                        )
                    }
                }

                // 페이지 전환에 따른 음원과 뮤비 재생 상태
                if (page != DETAIL_PICK_TAB) playerServiceViewModel.onPause()
            }
        }

        DetailPickUiState.Deleted -> {
            LaunchedEffect(Unit) {
                onBackClick()
                onDeleted(context)
                Toast.makeText(
                    context,
                    context.getString(R.string.success_delete_pick),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        DetailPickUiState.Error -> {
            LaunchedEffect(Unit) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_loading_pick_list),
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Show default pick
            DetailPick(
                pick = DEFAULT_PICK,
                isCreatedBySelf = false,
                isFavorite = false,
                userId = "",
                userName = "",
                favoriteCount = 0,
                isMusicVideoAvailable = false,
                playerServiceViewModel = playerServiceViewModel,
                onProfileClick = onProfileClick,
                onBackClick = onBackClick,
                onActionClick = { }
            )
        }
    }

    if (showDeletePickDialog) {
        AlertStringDialog(
            onDismissRequest = {
                showDeletePickDialog = false
            },
            title = stringResource(R.string.delete_pick_dialog_title),
            body = stringResource(R.string.delete_pick_dialog_body),
            buttons = {
                DialogTextButton(
                    onClick = {
                        showDeletePickDialog = false
                    },
                    text = stringResource(R.string.delete_pick_dialog_cancel)
                )

                HorizontalSpacer(8)

                DialogTextButton(
                    onClick = {
                        showDeletePickDialog = false
                        pickViewModel.deletePick(pickId)
                    },
                    text = stringResource(R.string.delete_pick_dialog_delete),
                    textColor = Primary,
                    fontWeight = FontWeight.Bold
                )
            },
        )
    }

    if (showProcessIndicator) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black.copy(alpha = 0.5F))
                .clickable( // 클릭 효과 제거 및 클릭 이벤트 무시
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun DetailPick(
    pick: Pick,
    isCreatedBySelf: Boolean,
    isFavorite: Boolean,
    userId: String,
    userName: String,
    favoriteCount: Int,
    isMusicVideoAvailable: Boolean,
    playerServiceViewModel: PlayerServiceViewModel,
    onProfileClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val dynamicBackgroundColor = Color(pick.song.bgColor)
    val onDynamicBackgroundColor = if (dynamicBackgroundColor.luminance() >= 0.5f) Black else White
    val view = LocalView.current
    val context = LocalContext.current

    val baseVisualizer = remember { BaseVisualizer() }

    val audioEffectColor = dynamicBackgroundColor.copy(
        red = (dynamicBackgroundColor.red + 0.2f).coerceAtMost(1.0f),
        green = (dynamicBackgroundColor.green + 0.2f).coerceAtMost(1.0f),
        blue = (dynamicBackgroundColor.blue + 0.2f).coerceAtMost(1.0f),
    )

    val playerUiState by playerServiceViewModel.playerState.collectAsStateWithLifecycle()
    val audioSessionId by playerServiceViewModel.audioSessionId.collectAsStateWithLifecycle(0)

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val windowInsetsController = WindowInsetsControllerCompat(window, view)
            val isLightStatusBar = dynamicBackgroundColor.luminance() >= 0.5f

            windowInsetsController.isAppearanceLightStatusBars = isLightStatusBar
        }
    }

    Scaffold(
        topBar = {
            DetailPickTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                isCreatedBySelf = isCreatedBySelf,
                isFavorite = isFavorite,
                userId = userId,
                userName = userName,
                onDynamicBackgroundColor = onDynamicBackgroundColor,
                onProfileClick = onProfileClick,
                onBackClick = {
                    onBackClick()
                },
                onActionClick = { onActionClick() }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to dynamicBackgroundColor,
                            0.47f to Black
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SongInfo(
                    song = pick.song,
                    dynamicOnBackgroundColor = onDynamicBackgroundColor,
                    modifier = Modifier.zIndex(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .align(Alignment.CenterHorizontally)
                        .zIndex(0f)
                ) {
                    if (audioSessionId != 0) {
                        CircleAlbumCover(
                            modifier = Modifier
                                .size(320.dp)
                                .align(Alignment.Center),
                            song = pick.song,
                            currentPosition = { playerUiState.currentPosition },
                            duration = { playerUiState.duration },
                            audioEffectColor = audioEffectColor,
                            baseVisualizer = { baseVisualizer },
                            audioSessionId = audioSessionId,
                            onSeekChanged = { timeMs ->
                                playerServiceViewModel.onSeekingFinished(timeMs)
                            },
                        )
                    }

                    if (isMusicVideoAvailable) {
                        MusicVideoKnob(
                            thumbnail = pick.musicVideoThumbnailUrl,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }

                PickInformation(
                    formattedDate = pick.createdAt,
                    favoriteCount = favoriteCount
                )

                CommentText(comment = pick.comment)

                VerticalSpacer(height = 8)
            }

            if (pick.song.previewUrl.isBlank().not()) {
                MusicPlayer(
                    song = pick.song,
                    playerState = playerUiState,
                    onSeekChanged = { timeMs ->
                        playerServiceViewModel.onSeekingFinished(timeMs)
                    },
                    onReplayForwardClick = { isForward ->
                        if (isForward) {
                            playerServiceViewModel.onAdvanceBy()
                        } else {
                            playerServiceViewModel.onRewindBy()
                        }
                    },
                    onPauseToggle = { song ->
                        playerServiceViewModel.togglePlayPause(song)
                    },
                )
            }
        }
    }
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Preview
@Composable
private fun DetailPickPreview() {
    DetailPick(
        pick = DEFAULT_PICK,
        isCreatedBySelf = false,
        isFavorite = false,
        userId = "",
        userName = "짱구",
        favoriteCount = 0,
        isMusicVideoAvailable = true,
        onProfileClick = {},
        playerServiceViewModel = hiltViewModel(),
        onBackClick = {},
        onActionClick = {},
    )
}

internal const val DETAIL_PICK_TAB = 0
internal const val MUSIC_VIDEO_TAB = 1
