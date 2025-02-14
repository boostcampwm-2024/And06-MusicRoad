package com.squirtles.domain.user.usecase

import com.squirtles.domain.user.LocalUserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    operator fun invoke() = localUserRepository.currentUser
}
