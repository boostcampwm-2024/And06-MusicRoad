package com.squirtles.musicroad.common.picklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick
import com.squirtles.domain.usecase.picklist.DeletePickListUseCaseTemplate
import com.squirtles.domain.usecase.picklist.FetchPickListUseCaseTemplate
import com.squirtles.domain.usecase.picklist.GetPickListOrderUseCaseTemplate
import com.squirtles.domain.usecase.picklist.SavePickListOrderUseCaseTemplate
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class PickListViewModel(
    val fetchPickListUseCase: FetchPickListUseCaseTemplate,
    val getPickListOrderUseCase: GetPickListOrderUseCaseTemplate,
    val savePickListOrderUseCase: SavePickListOrderUseCaseTemplate,
    val deletePickListUseCase: DeletePickListUseCaseTemplate
) : ViewModel() {

    private var pickList: List<Pick>? = null

    private val _pickListUiState = MutableStateFlow<PickListUiState>(PickListUiState.Loading)
    val pickListUiState = _pickListUiState.asStateFlow()

    private val _selectedPicksId = MutableStateFlow<Set<String>>(emptySet())
    val selectedPicksId = _selectedPicksId.asStateFlow()

    fun fetchPickList(userId: String) {
        viewModelScope.launch {
            fetchPickListUseCase(userId)
                .onSuccess { picks ->
                    pickList = picks
                    sortPickList(getPickListOrderUseCase())
                }
                .onFailure {
                    _pickListUiState.emit(PickListUiState.Error)
                }
        }
    }

    private fun sortPickList(order: Order) {
        pickList?.let { pickList ->
            val sortedList = when (order) {
                Order.LATEST -> pickList

                Order.OLDEST -> pickList.reversed()

                Order.FAVORITE_DESC ->
                    pickList.sortedByDescending { it.favoriteCount }
            }

            _pickListUiState.value = PickListUiState.Success(
                pickList = sortedList,
                order = order
            )
        }
    }

    fun setListOrder(order: Order) {
        viewModelScope.launch {
            savePickListOrderUseCase(order)
            sortPickList(order)
        }
    }

    fun toggleSelectedPick(pickId: String) {
        val curSelectedPicksId = _selectedPicksId.value
        _selectedPicksId.value =
            if (curSelectedPicksId.contains(pickId)) curSelectedPicksId - pickId else curSelectedPicksId + pickId
    }

    fun selectAllPicks() {
        pickList?.let { pickList ->
            _selectedPicksId.value = pickList.map { it.id }.toSet()
        }
    }

    fun deselectAllPicks() {
        _selectedPicksId.value = emptySet()
    }

    fun deleteSelectedPicks(userId: String) {
        viewModelScope.launch {
            _pickListUiState.value = PickListUiState.Loading

            val deleteJobList = _selectedPicksId.value.map { pickId ->
                async { deletePickListUseCase(pickId, userId) }
            }.awaitAll()

            deselectAllPicks()
            if (deleteJobList.all { it.isSuccess }) {
                fetchPickList(userId)
            } else {
                _pickListUiState.value = PickListUiState.Error
                Log.e("PickListViewModel", "[픽 목록] 다중 삭제 오류")
            }
        }
    }
}
