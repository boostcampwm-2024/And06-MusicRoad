package com.squirtles.domain.usecase.pick

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class FetchPickUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(pickId: String) =
        firebaseRepository.fetchPick(pickId)

    suspend operator fun invoke(lat: Double, lng: Double, radiusInM: Double) =
        firebaseRepository.fetchPicksInArea(lat, lng, radiusInM)
}
