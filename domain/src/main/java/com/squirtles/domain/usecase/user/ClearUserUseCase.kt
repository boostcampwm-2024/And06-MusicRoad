package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.repository.local.LocalUserRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    suspend operator fun invoke() = localUserRepository.clearUser()
}
