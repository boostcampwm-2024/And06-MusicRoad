package com.squirtles.musicroad.common.picklist

import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick

fun List<Pick>.setOrderedList(order: Order): PickListUiState.Success {
    return PickListUiState.Success(
        pickList = when (order) {
            Order.LATEST -> this

            Order.OLDEST -> this.reversed()

            Order.FAVORITE_DESC -> this.sortedByDescending { it.favoriteCount }
        },
        order = order
    )
}
