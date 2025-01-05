package com.squirtles.musicroad.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squirtles.domain.model.User
import com.squirtles.domain.usecase.user.GetCurrentUserUseCase
import com.squirtles.domain.usecase.user.FetchUserByIdUseCase
import com.squirtles.domain.usecase.user.FetchUserUseCase
import com.squirtles.domain.usecase.user.UpdateUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val fetchUserByIdUseCase: FetchUserByIdUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase
) : ViewModel() {

    private val _profileUser = MutableStateFlow(DEFAULT_USER)
    val profileUser = _profileUser.asStateFlow()

    val currentUser get() = getCurrentUserUseCase()

    private val _updateSuccess = MutableSharedFlow<Boolean>()
    val updateSuccess = _updateSuccess.asSharedFlow()

    fun getUserById(userId: String) {
        viewModelScope.launch {
            if (userId == currentUser.userId) {
                _profileUser.emit(currentUser)
            } else {
                val otherProfileUsr = fetchUserByIdUseCase(userId).getOrDefault(DEFAULT_USER)
                _profileUser.emit(otherProfileUsr)
            }
        }
    }

    fun updateUsername(newUserName: String) {
        viewModelScope.launch {
            val result = runCatching {
                updateUserNameUseCase(currentUser.userId, newUserName).getOrThrow()
                fetchUserUseCase(currentUser.userId).getOrThrow()
            }
            _updateSuccess.emit(result.isSuccess)
        }
    }
}

val DEFAULT_USER = User("", "", listOf())

