package com.squirtles.domain.usecase.user

import com.squirtles.domain.local.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() : Flow<String?> {
        return localRepository.userIdFromDataStore
    }
}
