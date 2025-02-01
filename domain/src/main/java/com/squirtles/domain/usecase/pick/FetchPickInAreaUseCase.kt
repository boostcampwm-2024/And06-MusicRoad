package com.squirtles.domain.usecase.pick

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class FetchPickInAreaUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(lat: Double, lng: Double, radiusInM: Double) =
        firebaseRepository.fetchPicksInArea(lat, lng, radiusInM)
}
