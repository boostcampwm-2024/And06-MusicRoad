package com.example.user.usecase

import com.example.user.LocalUserRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    suspend operator fun invoke() = localUserRepository.clearUser()
}
