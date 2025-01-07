package com.squirtles.musicroad.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.Pick
import com.squirtles.domain.usecase.favorite.FetchFavoritePicksUseCase
import com.squirtles.domain.usecase.order.GetFavoriteListOrderUseCase
import com.squirtles.domain.usecase.order.SaveFavoriteListOrderUseCase
import com.squirtles.musicroad.common.picklist.PickListUiState
import com.squirtles.musicroad.common.picklist.setOrderedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val fetchFavoritePicksUseCase: FetchFavoritePicksUseCase,
    private val getFavoriteListOrderUseCase: GetFavoriteListOrderUseCase,
    private val saveFavoriteListOrderUseCase: SaveFavoriteListOrderUseCase,
) : ViewModel() {

    private var favoriteList: List<Pick> = emptyList()

    private val _pickListUiState = MutableStateFlow<PickListUiState>(PickListUiState.Loading)
    val pickListUiState = _pickListUiState.asStateFlow()

    fun fetchFavoritePicks(userId: String) {
        viewModelScope.launch {
            fetchFavoritePicksUseCase(userId)
                .onSuccess { favoritePicks ->
                    favoriteList = favoritePicks
                    setList(getFavoriteListOrderUseCase())
                }
                .onFailure {
                    _pickListUiState.emit(PickListUiState.Error)
                }
        }
    }

    fun setListOrder(order: Order) {
        viewModelScope.launch {
            saveFavoriteListOrderUseCase(order)
            setList(order)
        }
    }

    private fun setList(order: Order) {
        _pickListUiState.value = favoriteList.setOrderedList(order)
    }
}
