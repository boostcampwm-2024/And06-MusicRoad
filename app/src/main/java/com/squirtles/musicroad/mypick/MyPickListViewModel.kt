package com.squirtles.musicroad.mypick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick
import com.squirtles.domain.usecase.mypick.FetchMyPicksUseCase
import com.squirtles.domain.usecase.order.GetMyPickListOrderUseCase
import com.squirtles.domain.usecase.order.SaveMyPickListOrderUseCase
import com.squirtles.musicroad.common.picklist.PickListUiState
import com.squirtles.musicroad.common.picklist.setOrderedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPickListViewModel @Inject constructor(
    private val fetchMyPicksUseCase: FetchMyPicksUseCase,
    private val getMyPickListOrderUseCase: GetMyPickListOrderUseCase,
    private val saveMyPickListOrderUseCase: SaveMyPickListOrderUseCase,
) : ViewModel() {

    private var myPickList: List<Pick> = emptyList()

    private val _pickListUiState = MutableStateFlow<PickListUiState>(PickListUiState.Loading)
    val pickListUiState = _pickListUiState.asStateFlow()

    fun fetchMyPicks(userId: String) {
        viewModelScope.launch {
            fetchMyPicksUseCase(userId)
                .onSuccess { myPicks ->
                    myPickList = myPicks
                    setList(getMyPickListOrderUseCase())
                }
                .onFailure {
                    _pickListUiState.emit(PickListUiState.Error)
                }
        }
    }

    fun setListOrder(order: Order) {
        viewModelScope.launch {
            saveMyPickListOrderUseCase(order)
            setList(order)
        }
    }

    private fun setList(order: Order) {
        _pickListUiState.value = myPickList.setOrderedList(order)
    }
}
