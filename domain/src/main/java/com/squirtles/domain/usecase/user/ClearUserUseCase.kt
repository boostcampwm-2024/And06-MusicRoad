package com.squirtles.domain.usecase.user

import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class ClearUserUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke() = localRepository.clearUser()
}
