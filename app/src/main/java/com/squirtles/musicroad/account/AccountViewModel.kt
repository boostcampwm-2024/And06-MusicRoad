package com.squirtles.musicroad.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.squirtles.domain.usecase.user.CreateGoogledIdUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val createGoogledIdUserUseCase: CreateGoogledIdUserUseCase
) : ViewModel() {

    fun createGoogleIdUser(credential: GoogleIdTokenCredential) {
        viewModelScope.launch {
            createGoogledIdUserUseCase(
                userId = credential.id,
                userName = credential.displayName,
                userProfileImage = credential.profilePictureUri.toString()
            )
        }
    }
}
