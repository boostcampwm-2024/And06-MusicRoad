package com.squirtles.musicroad.detail

import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.Creator
import com.squirtles.domain.model.LocationPoint
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.Song
import com.squirtles.domain.usecase.favorite.CreateFavoriteUseCase
import com.squirtles.domain.usecase.favorite.DeleteFavoriteUseCase
import com.squirtles.domain.usecase.mypick.DeletePickUseCase
import com.squirtles.domain.usecase.pick.FetchIsFavoriteUseCase
import com.squirtles.domain.usecase.pick.FetchPickUseCase
import com.squirtles.domain.usecase.user.GetCurrentUserUseCase
import com.squirtles.musicroad.common.throttleFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val fetchPickUseCase: FetchPickUseCase,
    private val fetchIsFavoriteUseCase: FetchIsFavoriteUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deletePickUseCase: DeletePickUseCase,
    private val createFavoriteUseCase: CreateFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
) : ViewModel() {

    private val _detailPickUiState = MutableStateFlow<DetailPickUiState>(DetailPickUiState.Loading)
    val detailPickUiState = _detailPickUiState.asStateFlow()

    private var _currentTab = DETAIL_PICK_TAB
    val currentTab get() = _currentTab

    private val _favoriteAction = MutableSharedFlow<FavoriteAction>()
    val favoriteAction = _favoriteAction.asSharedFlow()

    private val actionClick = MutableSharedFlow<Pair<String, Boolean>>()

    init {
        viewModelScope.launch {
            actionClick
                .throttleFirst(1000)
                .collect { (pickId, isAdding) ->
                    if (isAdding) {
                        addToFavoritePicks(pickId)
                    } else {
                        deleteFromFavoritePicks(pickId)
                    }
                }
        }
    }

    fun getUserId() = getCurrentUserUseCase().userId

    fun fetchPick(pickId: String) {
        viewModelScope.launch {
            val fetchPick = async {
                fetchPickUseCase(pickId)
            }
            val fetchIsFavorite = async {
                fetchIsFavoriteUseCase(pickId, getUserId())
            }

            val fetchPickResult = fetchPick.await()
            val fetchIsFavoriteResult = fetchIsFavorite.await()

            when {
                fetchPickResult.isSuccess && fetchIsFavoriteResult.isSuccess -> {
                    _detailPickUiState.emit(
                        DetailPickUiState.Success(
                            pick = fetchPickResult.getOrDefault(DEFAULT_PICK),
                            isFavorite = fetchIsFavoriteResult.getOrDefault(false)
                        )
                    )
                }

                else -> {
                    _detailPickUiState.emit(DetailPickUiState.Error)
                }
            }
        }
    }

    fun toggleFavoritePick(pickId: String, isAdding: Boolean) {
        viewModelScope.launch {
            actionClick.emit(pickId to isAdding)
        }
    }

    fun deletePick(pickId: String) {
        viewModelScope.launch {
            _detailPickUiState.emit(DetailPickUiState.Loading)
            deletePickUseCase(pickId, getUserId())
                .onSuccess {
                    _detailPickUiState.emit(DetailPickUiState.Deleted)
                }
                .onFailure {
                    _detailPickUiState.emit(DetailPickUiState.Error)
                }
        }
    }

    private fun addToFavoritePicks(pickId: String) {
        viewModelScope.launch {
            createFavoriteUseCase(pickId, getUserId())
                .onSuccess {
                    _favoriteAction.emit(FavoriteAction.ADDED)
                    val currentUiState = _detailPickUiState.value as? DetailPickUiState.Success
                    currentUiState?.let { successState ->
                        _detailPickUiState.emit(successState.copy(isFavorite = true))
                    }
                }
                .onFailure {
                    _detailPickUiState.emit(DetailPickUiState.Error)
                }
        }
    }

    private fun deleteFromFavoritePicks(pickId: String) {
        viewModelScope.launch {
            deleteFavoriteUseCase(pickId, getUserId())
                .onSuccess {
                    _favoriteAction.emit(FavoriteAction.DELETED)
                    val currentUiState = _detailPickUiState.value as? DetailPickUiState.Success
                    currentUiState?.let { successState ->
                        _detailPickUiState.emit(successState.copy(isFavorite = false))
                    }
                }
                .onFailure {
                    _detailPickUiState.emit(DetailPickUiState.Error)
                }
        }
    }

    fun setCurrentTab(index: Int) {
        _currentTab = index
    }

    companion object {
        val DEFAULT_PICK =
            Pick(
                id = "",
                song = Song(
                    id = "",
                    songName = "",
                    artistName = "",
                    albumName = "",
                    imageUrl = "",
                    genreNames = listOf(),
                    bgColor = "#000000".toColorInt(),
                    externalUrl = "",
                    previewUrl = ""
                ),
                comment = "",
                createdAt = "",
                createdBy = Creator(userId = "", userName = "짱구"),
                favoriteCount = 0,
                location = LocationPoint(1.0, 1.0),
                musicVideoUrl = "",
            )
    }
}
