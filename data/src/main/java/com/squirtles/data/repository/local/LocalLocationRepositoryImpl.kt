package com.squirtles.data.repository.local

import android.location.Location
import com.squirtles.domain.repository.local.LocalLocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Singleton

@Singleton
class LocalLocationRepositoryImpl: LocalLocationRepository {
    private var _currentLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    override val lastLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    override suspend fun saveCurrentLocation(location: Location) {
        _currentLocation.emit(location)
    }
}
