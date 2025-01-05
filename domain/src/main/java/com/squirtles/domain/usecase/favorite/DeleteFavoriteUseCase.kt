package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(pickId: String, userId: String) =
        firebaseRepository.deleteFavorite(pickId, userId)
}
