package com.example.favorite.usecase

import com.example.favorite.FirebaseFavoriteRepository
import com.example.picklist.RemovePickUseCaseInterface
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) : RemovePickUseCaseInterface {
    override suspend operator fun invoke(pickId: String, userId: String) =
        favoriteRepository.deleteFavorite(pickId, userId)
}
