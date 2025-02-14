package com.squirtles.domain.favorite.usecase

import com.squirtles.domain.favorite.FirebaseFavoriteRepository
import com.squirtles.domain.picklist.FetchPickListUseCaseInterface
import javax.inject.Inject

class FetchFavoritePicksUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) : FetchPickListUseCaseInterface {
    override suspend operator fun invoke(userId: String) = favoriteRepository.fetchFavoritePicks(userId)
}
