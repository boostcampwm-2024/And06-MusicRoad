package com.squirtles.domain.usecase.user

import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, newUserName: String) =
        firebaseRepository.updateUserName(userId, newUserName)
}
