package com.example.location.usecase

import com.example.location.LocalLocationRepository
import javax.inject.Inject

class GetLastLocationUseCase @Inject constructor(
    private val localLocationRepository: LocalLocationRepository
) {
    operator fun invoke() = localLocationRepository.lastLocation
}
