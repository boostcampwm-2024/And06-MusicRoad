package com.squirtles.domain.usecase.user

import com.squirtles.domain.model.User
import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val firebaseRepository: FirebaseRepository
) {
    suspend operator fun invoke(userId: String): Result<User> {
        // userId가 있으면 Firestore에서 유저 가져오기
        val user = firebaseRepository.fetchUser(userId)
            .onSuccess { user ->
                localRepository.saveCurrentUser(user)
            }
        return user
    }
}
