package com.squirtles.musicroad.pick

import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.Creator
import com.squirtles.domain.model.LocationPoint
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.Song
import com.squirtles.domain.usecase.favoritepick.CreateFavoriteUseCase
import com.squirtles.domain.usecase.favoritepick.DeleteFavoriteUseCase
import com.squirtles.domain.usecase.local.GetCurrentUserUseCase
import com.squirtles.domain.usecase.mypick.DeletePickUseCase
import com.squirtles.domain.usecase.pick.FetchIsFavoriteUseCase
import com.squirtles.domain.usecase.pick.FetchPickUseCase
import com.squirtles.musicroad.common.throttleFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class FavoriteAction {
    ADDED, DELETED
}

@HiltViewModel
class PickViewModel @Inject constructor(
    private val fetchPickUseCase: FetchPickUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val deletePickUseCase: DeletePickUseCase,
    private val fetchIsFavoriteUseCase: FetchIsFavoriteUseCase,
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
                    getUserId()?.let { userId ->
                        if (isAdding) {
                            addToFavoritePicks(pickId, userId)
                        } else {
                            deleteFromFavoritePicks(pickId, userId)
                        }
                    }
                }
        }
    }

    fun getUserId() = getCurrentUserUseCase()?.userId

    fun fetchPick(pickId: String) {
        viewModelScope.launch {
            _detailPickUiState.emit(DetailPickUiState.Loading)

            val fetchPick = async { fetchPickUseCase(pickId) }

            val fetchIsFavoriteResult: Result<Boolean> = getUserId()?.let {
                async { fetchIsFavoriteUseCase(pickId, it) }.await()
            } ?: Result.success(false) // 비회원일시 항상 false
            val fetchPickResult = fetchPick.await()

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
            getUserId()?.let { userId ->
                _detailPickUiState.emit(DetailPickUiState.Loading)
                deletePickUseCase(pickId, userId)
                    .onSuccess {
                        _detailPickUiState.emit(DetailPickUiState.Deleted)
                    }
                    .onFailure {
                        _detailPickUiState.emit(DetailPickUiState.Error)
                    }
            }
        }
    }

    private fun addToFavoritePicks(pickId: String, userId: String) {
        viewModelScope.launch {
            createFavoriteUseCase(pickId, userId)
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

    private fun deleteFromFavoritePicks(pickId: String, userId: String) {
        viewModelScope.launch {
            deleteFavoriteUseCase(pickId, userId)
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
