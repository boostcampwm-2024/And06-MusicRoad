package com.squirtles.domain.usecase.user

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.model.User
import javax.inject.Inject

class CreateGoogleIdUserUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(
        userId: String,
        userName: String? = null,
        userProfileImage: String? = null
    ): Result<User> {
        val createdUser = firebaseRepository.createGoogleIdUser(userId, userName, userProfileImage)
            .onSuccess { user ->
                // 생성된 유저의 userId 저장 후 user 반환
                localRepository.saveUserIdDataStore(user.userId)
                localRepository.saveCurrentUser(user)
            }
        return createdUser
    }
}
