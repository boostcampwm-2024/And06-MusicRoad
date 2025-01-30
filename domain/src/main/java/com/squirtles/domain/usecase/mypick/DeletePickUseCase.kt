package com.squirtles.domain.usecase.mypick

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.usecase.picklist.DeletePickListUseCaseTemplate
import javax.inject.Inject

class DeletePickUseCase @Inject constructor(
    firebaseRepository: FirebaseRepository
) : DeletePickListUseCaseTemplate(firebaseRepository) {
    override suspend operator fun invoke(pickId: String, userId: String): Result<Boolean> =
        firebaseRepository.deletePick(pickId, userId)
}
