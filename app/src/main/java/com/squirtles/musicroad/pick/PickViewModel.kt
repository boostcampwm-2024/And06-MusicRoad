package com.squirtles.musicroad.pick

import androidx.core.graphics.toColorInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.LocationPoint
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.Song
import com.squirtles.domain.usecase.FetchPickUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val fetchPickUseCase: FetchPickUseCase
) : ViewModel() {

    private val pickId = savedStateHandle["pickId"] ?: ""

    private val _pick = MutableStateFlow(DEFAULT_PICK)
    val pick = _pick.asStateFlow()

    init {
        if (pickId.isNotBlank()) fetchPick(pickId)
    }

    private fun fetchPick(pickId: String) {
        viewModelScope.launch {
            fetchPickUseCase(pickId)
                .onSuccess { _pick.emit(it) }
                .onFailure { _pick.emit(DEFAULT_PICK) }
        }
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
                createdBy = "",
                favoriteCount = 0,
                location = LocationPoint(1.0, 1.0),
                musicVideoUrl = "",
            )
    }
}
