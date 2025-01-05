package com.squirtles.domain.usecase.user

import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.model.User
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val fetchUserByIdUseCase: FetchUserByIdUseCase
) {
    suspend operator fun invoke(userId: String): Result<User> {
        // userId가 있으면 Firestore에서 유저 가져오기
        val user = fetchUserByIdUseCase(userId)
            .onSuccess { user ->
                localRepository.saveCurrentUser(user)
            }
        return user
    }
}
