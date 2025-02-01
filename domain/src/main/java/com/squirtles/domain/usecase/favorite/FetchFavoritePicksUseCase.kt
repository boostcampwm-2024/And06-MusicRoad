package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.usecase.picklist.FetchPickListUseCaseTemplate
import javax.inject.Inject

class FetchFavoritePicksUseCase @Inject constructor(
    firebaseRepository: FirebaseRepository
) : FetchPickListUseCaseTemplate(firebaseRepository) {
    override suspend operator fun invoke(userId: String) = firebaseRepository.fetchFavoritePicks(userId)
}
