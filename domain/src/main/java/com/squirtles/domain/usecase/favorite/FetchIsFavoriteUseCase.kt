package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.remote.firebase.FirebaseFavoriteRepository
import javax.inject.Inject

class FetchIsFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) {
    suspend operator fun invoke(pickId: String, userId: String) =
        favoriteRepository.fetchIsFavorite(pickId, userId)
}
