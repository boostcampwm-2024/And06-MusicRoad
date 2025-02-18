package com.example.user.usecase

import com.example.user.LocalUserRepository
import javax.inject.Inject

class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository
) {
    operator fun invoke() = localUserRepository.readUserIdDataStore()
}
