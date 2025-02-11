package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.repository.remote.firebase.FirebaseFavoriteRepository
import com.squirtles.domain.usecase.picklist.FetchPickListUseCaseInterface
import javax.inject.Inject

class FetchFavoritePicksUseCase @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository
) : FetchPickListUseCaseInterface {
    override suspend operator fun invoke(userId: String) = favoriteRepository.fetchFavoritePicks(userId)
}
