package com.squirtles.domain.user.usecase

import com.squirtles.domain.user.LocalUserRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    suspend operator fun invoke() = localUserRepository.clearUser()
}
