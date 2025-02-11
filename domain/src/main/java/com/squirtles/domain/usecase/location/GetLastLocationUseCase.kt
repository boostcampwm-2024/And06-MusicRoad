package com.squirtles.domain.usecase.location

import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class GetLastLocationUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() = localRepository.lastLocation
}
