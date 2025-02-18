package com.example.user.usecase

import com.example.user.FirebaseUserRepository
import javax.inject.Inject

class FetchUserByIdUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(userId: String) =
        firebaseUserRepository.fetchUser(userId)
}
