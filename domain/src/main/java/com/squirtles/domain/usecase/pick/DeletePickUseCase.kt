package com.squirtles.domain.usecase.pick

import com.squirtles.domain.remote.firebase.FirebasePickRepository
import com.squirtles.domain.usecase.picklist.RemovePickUseCaseInterface
import javax.inject.Inject

class DeletePickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) : RemovePickUseCaseInterface {
    override suspend operator fun invoke(pickId: String, userId: String): Result<Boolean> =
        pickRepository.deletePick(pickId, userId)
}
