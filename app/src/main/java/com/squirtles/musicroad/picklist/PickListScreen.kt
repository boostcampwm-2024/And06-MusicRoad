package com.squirtles.musicroad.picklist

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.CircularProgressIndicator
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.Constants.COLOR_STOPS
import com.squirtles.musicroad.common.Constants.DEFAULT_PADDING
import com.squirtles.musicroad.common.DefaultTopAppBar
import com.squirtles.musicroad.common.TotalCountText
import com.squirtles.musicroad.common.VerticalSpacer
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White

@Composable
fun PickListScreen(
    userId: String,
    isFavoritePicks: Boolean,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    pickListViewModel: PickListViewModel = hiltViewModel()
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    val uiState by pickListViewModel.pickListUiState.collectAsStateWithLifecycle()
    var showOrderBottomSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isFavoritePicks) {
            pickListViewModel.fetchFavoritePicks(userId)
        } else {
            pickListViewModel.fetchMyPicks(userId)
        }
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(
                    if (isFavoritePicks) R.string.favorite_picks_top_app_bar_title else R.string.my_picks_top_app_bar_title
                ),
                onBackClick = onBackClick
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops = COLOR_STOPS))
                .padding(innerPadding)
                .then(if (isLandscape) Modifier.displayCutoutPadding() else Modifier)
        ) {
            when (uiState) {
                PickListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(36.dp)
                            .align(Alignment.Center),
                        indicatorColor = Primary
                    )
                }

                is PickListUiState.Success -> {
                    val pickList = (uiState as PickListUiState.Success).pickList
                    val order = (uiState as PickListUiState.Success).order

                    PickList(
                        isFavoritePicks = isFavoritePicks,
                        pickList = pickList,
                        order = order,
                        onOrderClick = { showOrderBottomSheet = true },
                        onItemClick = onItemClick
                    )
                }

                PickListUiState.Error -> {
                    Text(
                        text = stringResource(R.string.error_loading_pick_list),
                        modifier = Modifier.align(Alignment.Center),
                        color = White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    // TODO: 다시하기 버튼 같은 거 만들어서 요청 다시 하게 할 수 있도록 만드는 것도 고려해보기
                }
            }
        }
    }

    if (showOrderBottomSheet) {
        OrderBottomSheet(
            isFavoritePicks = isFavoritePicks,
            currentOrder = (uiState as PickListUiState.Success).order,
            onDismissRequest = { showOrderBottomSheet = false },
            onOrderClick = { order ->
                pickListViewModel.setListOrder(isFavoritePicks, order)
            },
        )
    }
}

@Composable
private fun PickList(
    isFavoritePicks: Boolean,
    pickList: List<Pick>,
    order: Order,
    onOrderClick: () -> Unit,
    onItemClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DEFAULT_PADDING),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TotalCountText(
                totalCount = pickList.size,
                defaultColor = White,
            )

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .clip(CircleShape)
                    .clickable { onOrderClick() }
            ) {
                Text(
                    text = "${
                        stringResource(
                            when (order) {
                                Order.LATEST ->
                                    if (isFavoritePicks) R.string.latest_favorite_order else R.string.latest_create_order

                                Order.OLDEST ->
                                    if (isFavoritePicks) R.string.oldest_favorite_order else R.string.oldest_create_order

                                Order.FAVORITE_DESC -> R.string.favorite_count_desc
                            }
                        )
                    }  ▼",
                    modifier = Modifier.padding(
                        horizontal = DEFAULT_PADDING / 2,
                        vertical = DEFAULT_PADDING / 4
                    ),
                    color = White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        VerticalSpacer(8)

        if (pickList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(
                        if (isFavoritePicks) R.string.favorite_picks_empty else R.string.my_picks_empty
                    ),
                    color = White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = pickList,
                    key = { it.id }
                ) { pick ->
                    PickItem(
                        song = pick.song,
                        createdByOthers = isFavoritePicks,
                        createUserName = pick.createdBy.userName,
                        favoriteCount = pick.favoriteCount,
                        comment = pick.comment,
                        createdAt = pick.createdAt,
                        onItemClick = { onItemClick(pick.id) }
                    )
                }
            }
        }
    }
}
