package com.example.user.usecase

import com.example.user.FirebaseUserRepository
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(userId: String, newUserName: String) =
        firebaseUserRepository.updateUserName(userId, newUserName)
}
