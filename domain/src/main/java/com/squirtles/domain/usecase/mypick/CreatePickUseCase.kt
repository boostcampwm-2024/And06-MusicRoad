package com.squirtles.domain.usecase.mypick

import com.squirtles.domain.model.Pick
import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class CreatePickUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(pick: Pick): Result<String> = firebaseRepository.createPick(pick)
}
