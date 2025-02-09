package com.squirtles.musicroad.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.firebase.FirebaseException
import com.squirtles.domain.usecase.user.FetchUserUseCase
import com.squirtles.domain.usecase.user.GetUserIdFromDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserIdFromDataStoreUseCase: GetUserIdFromDataStoreUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private var _canRequestPermission = true
    val canRequestPermission get() = _canRequestPermission

    private val _localUserId = getUserIdFromDataStoreUseCase()

    init {
        viewModelScope.launch {
            _localUserId.collect { localUid ->
                if (localUid == null) { // 비회원 상태
                    _loadingState.emit(LoadingState.Success(null))
                } else {
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
