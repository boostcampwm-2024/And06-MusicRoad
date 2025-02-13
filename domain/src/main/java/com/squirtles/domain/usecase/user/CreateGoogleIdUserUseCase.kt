package com.squirtles.domain.usecase.user

import com.squirtles.domain.model.User
import com.squirtles.domain.repository.local.LocalUserRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
import javax.inject.Inject

class CreateGoogleIdUserUseCase @Inject constructor(
    private val localUserRepository: LocalUserRepository,
    private val firebaseUserRepository: FirebaseUserRepository
) {
    suspend operator fun invoke(
        userId: String,
        userName: String? = null,
        userProfileImage: String? = null
    ): Result<User> {
        val createdUser = firebaseUserRepository.createGoogleIdUser(userId, userName, userProfileImage)
            .onSuccess { user ->
                // 생성된 유저의 userId 저장 후 user 반환
                localUserRepository.saveUserIdDataStore(user.userId)
                localUserRepository.saveCurrentUser(user)
            }
        return createdUser
    }
}
