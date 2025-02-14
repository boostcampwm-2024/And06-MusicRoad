package com.squirtles.domain.pick.usecase

import com.squirtles.domain.pick.FirebasePickRepository
import javax.inject.Inject

class FetchPickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) {
    suspend operator fun invoke(pickId: String) =
        pickRepository.fetchPick(pickId)

    suspend operator fun invoke(lat: Double, lng: Double, radiusInM: Double) =
        pickRepository.fetchPicksInArea(lat, lng, radiusInM)
}
