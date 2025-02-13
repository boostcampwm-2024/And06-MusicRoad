package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val firebaseUserRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(userId: String, newUserName: String) =
        firebaseUserRepository.updateUserName(userId, newUserName)
}
