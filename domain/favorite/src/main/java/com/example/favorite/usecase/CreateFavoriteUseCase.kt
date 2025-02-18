package com.example.favorite.usecase

import com.example.favorite.FirebaseFavoriteRepository
import javax.inject.Inject

class CreateFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) {
    suspend operator fun invoke(pickId: String, userId: String) =
        favoriteRepository.createFavorite(pickId, userId)
}
