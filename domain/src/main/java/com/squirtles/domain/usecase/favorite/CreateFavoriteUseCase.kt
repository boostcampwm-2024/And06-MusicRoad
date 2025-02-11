package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.repository.remote.firebase.FirebaseFavoriteRepository
import javax.inject.Inject

class CreateFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) {
    suspend operator fun invoke(pickId: String, userId: String) =
        favoriteRepository.createFavorite(pickId, userId)
}
