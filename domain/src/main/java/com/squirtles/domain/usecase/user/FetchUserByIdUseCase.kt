package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
import javax.inject.Inject

class FetchUserByIdUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(userId: String) =
        firebaseUserRepository.fetchUser(userId)
}
