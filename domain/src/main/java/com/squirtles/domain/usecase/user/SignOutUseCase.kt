package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.LocalRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke() = localRepository.clearUser()
}
