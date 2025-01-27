package com.example.picklist

import com.example.model.Order
import com.example.model.Pick

sealed class PickListUiState {
    data object Loading : PickListUiState()
    data class Success(val pickList: List<Pick>, val order: Order) : PickListUiState()
    data object Error : PickListUiState()
}
