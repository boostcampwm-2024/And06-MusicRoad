package com.squirtles.domain.usecase.favorite

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.usecase.picklist.DeletePickListUseCaseTemplate
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    firebaseRepository: FirebaseRepository
) : DeletePickListUseCaseTemplate(firebaseRepository) {
    override suspend operator fun invoke(pickId: String, userId: String) =
        firebaseRepository.deleteFavorite(pickId, userId)
}
