package com.squirtles.domain.usecase.location

import android.location.Location
import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class SaveLastLocationUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(location: Location) = localRepository.saveCurrentLocation(location)
}
