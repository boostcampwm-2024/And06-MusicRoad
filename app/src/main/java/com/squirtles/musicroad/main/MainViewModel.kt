package com.squirtles.musicroad.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.squirtles.domain.firebase.FirebaseException
import com.squirtles.domain.usecase.user.FetchUserByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fetchUserByIdUseCase: FetchUserByIdUseCase
) : ViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    val loadingState = _loadingState.asStateFlow()

    private var _canRequestPermission = true
    val canRequestPermission get() = _canRequestPermission

    init {
        viewModelScope.launch {
            FirebaseAuth.getInstance().currentUser?.uid.let { uid ->
                Log.d("AutoLogin", "현재 uid : $uid")
                if (uid == null) { // 비로그인 상태
                    _loadingState.emit(LoadingState.Success(null))
                } else {
                    fetchUser(uid)
                }
            }
        }
    }

    fun setCanRequestPermission(canRequest: Boolean) {
        _canRequestPermission = canRequest
    }

    private suspend fun fetchUser(uid: String) {
        fetchUserByIdUseCase(uid)
            .onSuccess {
                _loadingState.emit(LoadingState.Success(it.uid))
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
