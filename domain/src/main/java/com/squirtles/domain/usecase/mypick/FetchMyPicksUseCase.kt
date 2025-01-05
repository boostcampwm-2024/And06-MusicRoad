package com.squirtles.domain.usecase.mypick

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class FetchMyPicksUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String) =
        firebaseRepository.fetchMyPicks(userId)
}
