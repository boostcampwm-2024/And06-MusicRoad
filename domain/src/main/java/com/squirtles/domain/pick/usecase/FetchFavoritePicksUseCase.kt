package com.squirtles.domain.pick.usecase

import com.squirtles.domain.pick.FirebasePickRepository
import com.squirtles.domain.picklist.FetchPickListUseCaseInterface
import javax.inject.Inject

class FetchFavoritePicksUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) : FetchPickListUseCaseInterface {
    override suspend operator fun invoke(userId: String) = pickRepository.fetchFavoritePicks(userId)
}
