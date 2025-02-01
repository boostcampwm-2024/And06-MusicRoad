package com.squirtles.domain.usecase.pick

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class FetchIsFavoriteUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(pickId: String, userId: String) =
        firebaseRepository.fetchIsFavorite(pickId, userId)
}
