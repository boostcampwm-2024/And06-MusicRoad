package com.squirtles.domain.usecase.pick

import com.squirtles.domain.remote.firebase.FirebasePickRepository
import javax.inject.Inject

class FetchPickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) {
    suspend operator fun invoke(pickId: String) =
        pickRepository.fetchPick(pickId)

    suspend operator fun invoke(lat: Double, lng: Double, radiusInM: Double) =
        pickRepository.fetchPicksInArea(lat, lng, radiusInM)
}
