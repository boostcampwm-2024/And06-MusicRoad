package com.example.user.usecase

import com.example.user.LocalUserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    operator fun invoke() = localUserRepository.currentUser
}
