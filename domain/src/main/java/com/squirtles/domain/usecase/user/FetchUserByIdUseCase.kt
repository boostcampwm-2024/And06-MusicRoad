package com.squirtles.domain.usecase.user

import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
import javax.inject.Inject

class FetchUserByIdUseCase @Inject constructor(
    private val userRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(userId: String) =
        userRepository.fetchUser(userId)
}
