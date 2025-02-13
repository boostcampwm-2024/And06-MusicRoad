package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.local.LocalUserRepository
import javax.inject.Inject

class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    operator fun invoke() = localUserRepository.readUserIdDataStore()
}
