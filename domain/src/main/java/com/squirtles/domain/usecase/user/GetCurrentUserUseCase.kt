package com.squirtles.domain.usecase.user

import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() = localRepository.currentUser
}
