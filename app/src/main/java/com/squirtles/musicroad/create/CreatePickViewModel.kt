package com.squirtles.musicroad.create

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.squirtles.domain.model.Creator
import com.squirtles.domain.model.LocationPoint
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.Song
import com.squirtles.domain.usecase.location.GetLastLocationUseCase
import com.squirtles.domain.usecase.user.GetCurrentUserUseCase
import com.squirtles.domain.usecase.music.FetchMusicVideoUseCase
import com.squirtles.domain.usecase.music.FetchSongsUseCase
import com.squirtles.domain.usecase.mypick.CreatePickUseCase
import com.squirtles.musicroad.utils.throttleFirst
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CreatePickViewModel @Inject constructor(
    getLastLocationUseCase: GetLastLocationUseCase,
    private val fetchSongsUseCase: FetchSongsUseCase,
    private val fetchMusicVideoUseCase: FetchMusicVideoUseCase,
    private val createPickUseCase: CreatePickUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    // SearchMusicScreen
    private val _searchUiState = MutableStateFlow<SearchUiState>(SearchUiState.HotResult)
    val searchUiState = _searchUiState.asStateFlow()

    private val _searchResult = MutableStateFlow<PagingData<Song>>(PagingData.empty())
    val searchResult = _searchResult.asStateFlow()

    private var _selectedSong: Song? = null
    val selectedSong get() = _selectedSong

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private var searchJob: Job? = null

    // CreatePickScreen
    private val _createPickUiState = MutableStateFlow<CreateUiState<String>>(CreateUiState.Default)
    val createPickUiState = _createPickUiState.asStateFlow()

    private val _comment = MutableStateFlow("")
    val comment get() = _comment

    private var lastLocation: Location? = null
    private val createPickClick = MutableSharedFlow<Unit>()

    init {
        // 데이터소스의 위치값을 계속 collect하며 curLocation 변수에 저장
        viewModelScope.launch {
            getLastLocationUseCase().collect { location ->
                lastLocation = location
            }
        }

        viewModelScope.launch {
            _searchText
                .debounce(300)
                .collect { searchKeyword ->
                    searchJob?.cancel()
                    if (searchKeyword.isNotBlank()) {
                        searchJob = launch { searchSongs(searchKeyword) }
                    } else {
                        _searchUiState.emit(SearchUiState.HotResult)
                    }
                }
        }

        // 등록 버튼 클릭 후 3초 이내의 클릭은 무시하고 픽 생성하기
        viewModelScope.launch {
            createPickClick
                .throttleFirst(3000)
                .collect {
                    createPick()
                }
        }
    }

    private suspend fun searchSongs(searchKeyword: String) {
        fetchSongsUseCase(searchKeyword)
            .cachedIn(viewModelScope)
            .collectLatest {
                _searchResult.emit(it)
                _searchUiState.emit(SearchUiState.SearchResult)
            }
    }

    fun onSongItemClick(song: Song) {
        _selectedSong = song
    }

    fun onCommentChange(text: String) {
        _comment.value = text
    }

    fun resetComment() {
        _comment.value = ""
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onCreatePickClick() {
        viewModelScope.launch {
            createPickClick.emit(Unit)
        }
    }

    private fun createPick() {
        _selectedSong?.let { song ->
            viewModelScope.launch {
                if (lastLocation == null) {
                    /* TODO: DEFAULT 인 경우 -> LocalDataSource 위치 데이터 못 불러옴 */
                    return@launch
                }

                val musicVideo = fetchMusicVideoUseCase(song)

                /* 등록 결과 - pick ID 담긴 Result */
                val user = getCurrentUserUseCase()
                val createResult = createPickUseCase(
                    Pick(
                        id = "",
                        song = song,
                        comment = _comment.value,
                        createdAt = "",
                        createdBy = Creator(
                            userId = user.userId,
                            userName = user.userName
                        ),
                        location = LocationPoint(lastLocation!!.latitude, lastLocation!!.longitude),
                        musicVideoUrl = musicVideo?.previewUrl ?: "",
                        musicVideoThumbnailUrl = musicVideo?.thumbnailUrl ?: ""
                    )
                )

                createResult.onSuccess { pickId ->
                    _createPickUiState.emit(CreateUiState.Success(pickId))
                }.onFailure {
                    /* TODO: Firestore 등록 실패처리 */
                    _createPickUiState.emit(CreateUiState.Error)
                    Log.d("CreatePickViewModel", createResult.exceptionOrNull()?.message.toString())
                }
            }
        }
    }
}
