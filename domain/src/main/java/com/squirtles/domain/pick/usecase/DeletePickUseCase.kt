package com.squirtles.domain.pick.usecase

import com.squirtles.domain.pick.FirebasePickRepository
import com.squirtles.domain.picklist.RemovePickUseCaseInterface
import javax.inject.Inject

class DeletePickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) : RemovePickUseCaseInterface {
    override suspend operator fun invoke(pickId: String, userId: String): Result<Boolean> =
        pickRepository.deletePick(pickId, userId)
}
