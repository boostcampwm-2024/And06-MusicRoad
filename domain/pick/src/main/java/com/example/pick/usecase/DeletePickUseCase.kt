package com.example.pick.usecase

import com.example.pick.FirebasePickRepository
import com.example.picklist.RemovePickUseCaseInterface
import javax.inject.Inject

class DeletePickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) : RemovePickUseCaseInterface {
    override suspend operator fun invoke(pickId: String, userId: String): Result<Boolean> =
        pickRepository.deletePick(pickId, userId)
}
