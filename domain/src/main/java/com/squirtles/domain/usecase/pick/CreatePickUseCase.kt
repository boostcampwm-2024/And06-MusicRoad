package com.squirtles.domain.usecase.pick

import com.squirtles.domain.model.Pick
import com.squirtles.domain.remote.firebase.FirebasePickRepository
import javax.inject.Inject

class CreatePickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) {
    suspend operator fun invoke(pick: Pick): Result<String> = pickRepository.createPick(pick)
}
