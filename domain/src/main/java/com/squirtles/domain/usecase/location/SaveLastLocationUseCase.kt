package com.squirtles.domain.usecase.location

import android.location.Location
import com.squirtles.domain.repository.local.LocalLocationRepository
import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class SaveLastLocationUseCase @Inject constructor(
    private val localLocationRepository: LocalLocationRepository
) {
    suspend operator fun invoke(location: Location) = localLocationRepository.saveCurrentLocation(location)
}
