package com.squirtles.musicroad.picklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.squirtles.domain.model.Order
import com.squirtles.musicroad.R
import com.squirtles.musicroad.common.Constants.DEFAULT_PADDING
import com.squirtles.musicroad.ui.theme.Dark
import com.squirtles.musicroad.ui.theme.Primary
import com.squirtles.musicroad.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortListBottomSheet(
    isFavoritePicks: Boolean,
    currentOrder: Order,
    onDismissRequest: () -> Unit,
    onOrderClick: (Order) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val orderList = listOf(
        stringResource(if (isFavoritePicks) R.string.latest_favorite_order else R.string.latest_create_order) to Order.LATEST,
        stringResource(if (isFavoritePicks) R.string.oldest_favorite_order else R.string.oldest_create_order) to Order.OLDEST,
        stringResource(R.string.favorite_count_desc) to Order.FAVORITE_DESC,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .displayCutoutPadding(),
        // FIXME: navigationBarsPadding을 가로모드에서만 적용하기. 세로모드일 때도 적용하면 바텀 시트가 더 올라와서 이상해보임
        sheetState = sheetState,
        containerColor = Dark,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            orderList.forEach { (orderText, order) ->
                BottomSheetMenu(
                    text = orderText,
                    isSelected = currentOrder == order,
                ) {
                    scope
                        .launch {
                            if (currentOrder != order) {
                                onOrderClick(order)
                            }
                            sheetState.hide()
                        }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                onDismissRequest()
                            }
                        }
                }
            }

            HorizontalDivider(color = White)
            BottomSheetMenu(
                text = stringResource(R.string.close_button_text),
                textAlign = TextAlign.Center,
            ) {
                scope
                    .launch { sheetState.hide() }
                    .invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            onDismissRequest()
                        }
                    }
            }
        }
    }
}

@Composable
private fun BottomSheetMenu(
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(DEFAULT_PADDING),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = if (isSelected) Primary else White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            textAlign = textAlign,
        )

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.selected_icon_description),
                tint = Primary
            )
        }
    }
}
