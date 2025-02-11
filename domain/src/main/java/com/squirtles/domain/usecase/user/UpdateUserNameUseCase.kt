package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val userRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(userId: String, newUserName: String) =
        userRepository.updateUserName(userId, newUserName)
}
