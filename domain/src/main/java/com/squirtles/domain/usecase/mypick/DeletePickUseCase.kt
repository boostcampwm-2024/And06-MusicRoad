package com.squirtles.domain.usecase.mypick

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class DeletePickUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(pickId: String, userId: String): Result<Boolean> =
        firebaseRepository.deletePick(pickId, userId)
}
