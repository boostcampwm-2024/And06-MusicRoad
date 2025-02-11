package com.squirtles.musicroad.main

sealed class LoadingState {
    data object Loading : LoadingState()
    data class Success(val userId: String?) : LoadingState()
    data class NetworkError(val error: String) : LoadingState()
    data class CreatedUserError(val error: String) : LoadingState()
    data class UserNotFoundError(val error: String) : LoadingState()
}
