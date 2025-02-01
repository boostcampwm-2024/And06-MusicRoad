package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.remote.firebase.FirebaseFavoriteRepository
import com.squirtles.domain.usecase.picklist.RemovePickUseCaseInterface
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) : RemovePickUseCaseInterface {
    override suspend operator fun invoke(pickId: String, userId: String) =
        favoriteRepository.deleteFavorite(pickId, userId)
}
