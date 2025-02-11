package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() = localRepository.readUserIdDataStore()
}
