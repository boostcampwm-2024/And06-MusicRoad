package com.squirtles.musicroad.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.squirtles.domain.usecase.user.CreateGoogledIdUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val createGoogledIdUserUseCase: CreateGoogledIdUserUseCase
) : ViewModel() {

    private val _createSuccess = MutableSharedFlow<Boolean>()
    val createSuccess = _createSuccess.asSharedFlow()

    fun createGoogleIdUser(credential: GoogleIdTokenCredential) {
        viewModelScope.launch {
            createGoogledIdUserUseCase(
                userId = credential.id,
                userName = credential.displayName,
                userProfileImage = credential.profilePictureUri.toString()
            ).onSuccess {
                _createSuccess.emit(true)
            }.onFailure {
                _createSuccess.emit(false)
            }
        }
    }
}
