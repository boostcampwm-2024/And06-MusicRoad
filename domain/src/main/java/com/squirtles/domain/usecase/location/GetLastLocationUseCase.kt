package com.squirtles.domain.usecase.location

import com.squirtles.domain.repository.local.LocalLocationRepository
import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class GetLastLocationUseCase @Inject constructor(
    private val localLocationRepository: LocalLocationRepository
) {
    operator fun invoke() = localLocationRepository.lastLocation
}
