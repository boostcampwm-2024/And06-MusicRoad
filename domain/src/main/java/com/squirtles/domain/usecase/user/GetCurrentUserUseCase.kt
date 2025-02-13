package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.repository.local.LocalUserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    operator fun invoke() = localUserRepository.currentUser
}
