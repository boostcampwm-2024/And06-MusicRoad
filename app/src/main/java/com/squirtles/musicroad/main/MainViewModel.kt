package com.squirtles.musicroad.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.exception.FirebaseException
import com.squirtles.domain.usecase.local.GetUserIdFromLocalStorageUseCase
import com.squirtles.domain.usecase.user.FetchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoadingState {
    data object Loading : LoadingState()
    data class Success(val userId: String?) : LoadingState()
    data class NetworkError(val error: String) : LoadingState()
    data class CreatedUserError(val error: String) : LoadingState()
    data class UserNotFoundError(val error: String) : LoadingState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserIdFromLocalStorageUseCase: GetUserIdFromLocalStorageUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private var _canRequestPermission = true
    val canRequestPermission get() = _canRequestPermission

    private val _localUserId = getUserIdFromLocalStorageUseCase()

    init {
        viewModelScope.launch {
            _localUserId.collect { localUid ->
                if (localUid == null) { // 비회원 상태
                    _loadingState.emit(LoadingState.Success(null))
                } else {
                    // TODO 구글 로그인 -> 자동 로그인
                    fetchUser(localUid)
                }
            }
        }
    }

    fun setCanRequestPermission(canRequest: Boolean) {
        _canRequestPermission = canRequest
    }

    private suspend fun fetchUser(userId: String) {
        fetchUserUseCase(userId)
            .onSuccess {
                _loadingState.emit(LoadingState.Success(it.userId))
            }
            .onFailure { exception ->
                when (exception) {
                    is FirebaseException.UserNotFoundException -> {
                        _loadingState.emit(LoadingState.UserNotFoundError(exception.message))
                    }

                    else -> {
                        _loadingState.emit(LoadingState.NetworkError(exception.message.toString()))
                    }
                }
            }
    }
}
