package com.squirtles.musicroad.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.firebase.FirebaseException
import com.squirtles.domain.usecase.user.GetUserIdFromDataStoreUseCase
import com.squirtles.domain.usecase.user.CreateNewUserUseCase
import com.squirtles.domain.usecase.user.FetchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getUserIdFromDataStoreUseCase: GetUserIdFromDataStoreUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val createNewUserUseCase: CreateNewUserUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private var _canRequestPermission = true
    val canRequestPermission get() = _canRequestPermission

    init {
//        viewModelScope.launch {
//            getUserIdFromDataStoreUseCase()
//                .catch { e ->
//                    Log.e("MainViewModel", "Error collecting flow: $e")
//                }
//                .collect { localUid ->
//                Log.d("MainViewModel", "localUid: $localUid")
//                if (localUid == null) {
//                    createUser()
//                } else {
//                    fetchUser(localUid)
//                }
//            }
//        }

        viewModelScope.launch {
            val userId = getUserIdFromDataStoreUseCase()
            if(userId == null) {
                createUser()
            } else {
                fetchUser(userId)
            }
        }
    }

    fun setCanRequestPermission(canRequest: Boolean) {
        _canRequestPermission = canRequest
    }

    private suspend fun createUser() {
        createNewUserUseCase()
            .onSuccess {
                _loadingState.emit(LoadingState.Success(it.userId))
            }
            .onFailure { exception ->
                when (exception) {
                    is FirebaseException.CreatedUserFailedException -> {
                        _loadingState.emit(LoadingState.CreatedUserError(exception.message))
                    }

                    else -> {
                        _loadingState.emit(LoadingState.NetworkError(exception.message.toString()))
                    }
                }
            }
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
