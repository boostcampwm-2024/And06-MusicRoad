package com.squirtles.musicroad.common.picklist

import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick

sealed class PickListUiState {
    data object Loading : PickListUiState()
    data class Success(val pickList: List<Pick>, val order: Order) : PickListUiState()
    data object Error : PickListUiState()
}
