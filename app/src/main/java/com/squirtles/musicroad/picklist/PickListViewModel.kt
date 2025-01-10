package com.squirtles.musicroad.picklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick
import com.squirtles.domain.usecase.favoritepick.DeleteFavoriteUseCase
import com.squirtles.domain.usecase.favoritepick.FetchFavoritePicksUseCase
import com.squirtles.domain.usecase.local.GetCurrentUserUseCase
import com.squirtles.domain.usecase.local.GetFavoriteListOrderUseCase
import com.squirtles.domain.usecase.local.GetMyListOrderUseCase
import com.squirtles.domain.usecase.local.SaveFavoriteListOrderUseCase
import com.squirtles.domain.usecase.local.SaveMyListOrderUseCase
import com.squirtles.domain.usecase.mypick.DeletePickUseCase
import com.squirtles.domain.usecase.mypick.FetchMyPicksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickListViewModel @Inject constructor(
    private val fetchFavoritePicksUseCase: FetchFavoritePicksUseCase,
    private val fetchMyPicksUseCase: FetchMyPicksUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getFavoriteListOrderUseCase: GetFavoriteListOrderUseCase,
    private val getMyListOrderUseCase: GetMyListOrderUseCase,
    private val saveFavoriteListOrderUseCase: SaveFavoriteListOrderUseCase,
    private val saveMyListOrderUseCase: SaveMyListOrderUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val deletePickUseCase: DeletePickUseCase,
) : ViewModel() {

    private var defaultList: List<Pick>? = null

    private val _pickListUiState = MutableStateFlow<PickListUiState>(PickListUiState.Loading)
    val pickListUiState = _pickListUiState.asStateFlow()

    private val _selectedPicksId = MutableStateFlow<Set<String>>(emptySet())
    val selectedPicksId = _selectedPicksId.asStateFlow()

    fun fetchFavoritePicks(userId: String) {
        viewModelScope.launch {
            fetchFavoritePicksUseCase(userId)
                .onSuccess { favoritePicks ->
                    defaultList = favoritePicks
                    setList(getFavoriteListOrderUseCase())
                }
                .onFailure {
                    _pickListUiState.emit(PickListUiState.Error)
                }
        }
    }

    fun fetchMyPicks(userId: String) {
        viewModelScope.launch {
            fetchMyPicksUseCase(userId)
                .onSuccess { myPicks ->
                    defaultList = myPicks
                    setList(getMyListOrderUseCase())
                }
                .onFailure {
                    _pickListUiState.emit(PickListUiState.Error)
                }
        }
    }

    fun setListOrder(type: PickListType, order: Order) {
        viewModelScope.launch {
            when (type) {
                PickListType.FAVORITE -> saveFavoriteListOrderUseCase(order)
                PickListType.CREATED -> saveMyListOrderUseCase(order)
            }
            setList(order)
        }
    }

    fun toggleSelectedPick(pickId: String) {
        val curSelectedPicksId = _selectedPicksId.value
        _selectedPicksId.value =
            if (curSelectedPicksId.contains(pickId)) curSelectedPicksId - pickId else curSelectedPicksId + pickId
    }

    fun selectAllPicks() {
        defaultList?.let { pickList ->
            _selectedPicksId.value = pickList.map { it.id }.toSet()
        }
    }

    fun deselectAllPicks() {
        _selectedPicksId.value = emptySet()
    }

    fun deleteSelectedPicks(type: PickListType) {
        viewModelScope.launch {
            _pickListUiState.value = PickListUiState.Loading

            val userId = getUserId()
            val deleteJobList = _selectedPicksId.value.map { pickId ->
                when (type) {
                    PickListType.FAVORITE -> async { deleteFavoriteUseCase(pickId, userId) }
                    PickListType.CREATED -> async { deletePickUseCase(pickId, userId) }
                }
            }
            val deleteJobResults = deleteJobList.awaitAll()

            deselectAllPicks()
            if (deleteJobResults.all { it.isSuccess }) {
                when (type) {
                    PickListType.FAVORITE -> fetchFavoritePicks(userId)
                    PickListType.CREATED -> fetchMyPicks(userId)
                }
            } else {
                _pickListUiState.value = PickListUiState.Error
                Log.e("PickListViewModel", "[픽 목록] 다중 삭제 오류")
            }
        }
    }

    private fun setList(order: Order) {
        defaultList?.let { pickList ->
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

    private fun getUserId() = getCurrentUserUseCase().userId
}
