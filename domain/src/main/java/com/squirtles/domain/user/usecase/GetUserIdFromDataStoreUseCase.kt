package com.squirtles.domain.user.usecase

import com.squirtles.domain.user.LocalUserRepository
import javax.inject.Inject

class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    operator fun invoke() = localUserRepository.readUserIdDataStore()
}
