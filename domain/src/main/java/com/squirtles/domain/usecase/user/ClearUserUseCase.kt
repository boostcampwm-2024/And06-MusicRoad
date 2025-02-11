package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke() = localRepository.clearUser()
}
