package com.example.picklist

import com.example.model.Order
import com.example.model.Pick

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
