package com.squirtles.domain.repository.local

import android.location.Location
import kotlinx.coroutines.flow.StateFlow

interface LocalLocationRepository {
    val lastLocation: StateFlow<Location?>

    suspend fun saveCurrentLocation(location: Location)
}
