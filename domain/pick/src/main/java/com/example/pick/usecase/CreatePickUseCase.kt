package com.example.pick.usecase

import com.example.model.Pick
import com.example.pick.FirebasePickRepository
import javax.inject.Inject

class CreatePickUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) {
    suspend operator fun invoke(pick: Pick): Result<String> = pickRepository.createPick(pick)
}
