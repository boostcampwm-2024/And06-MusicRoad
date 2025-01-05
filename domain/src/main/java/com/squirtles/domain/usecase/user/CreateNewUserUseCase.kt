package com.squirtles.domain.usecase.user

import com.squirtles.domain.model.User
import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class CreateNewUserUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val firebaseRepository: FirebaseRepository
) {

    suspend operator fun invoke(): Result<User> {
        val createdUser = firebaseRepository.createUser()
            .onSuccess { user ->
                // 생성된 유저의 userId 저장 후 user 반환
                localRepository.saveUserId(user.userId)
                localRepository.saveCurrentUser(user)
            }
        return createdUser
    }
}
