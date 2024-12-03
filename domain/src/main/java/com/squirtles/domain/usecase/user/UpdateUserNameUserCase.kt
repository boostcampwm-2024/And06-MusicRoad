package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.FirebaseRepository
import javax.inject.Inject

class UpdateUserNameUserCase @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String, newUserName: String) =
        firebaseRepository.updateUserName(userId, newUserName)
}
