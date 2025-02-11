package com.squirtles.domain.usecase.user

import com.squirtles.domain.model.User
import com.squirtles.domain.repository.local.LocalRepository
import javax.inject.Inject

class FetchUserUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val fetchUserByIdUseCase: FetchUserByIdUseCase
) {
    suspend operator fun invoke(userId: String): Result<User> {
        val user = fetchUserByIdUseCase(userId) // userId가 있으면 Firestore에서 유저 가져오기
            .onSuccess { user ->
                localRepository.saveUserIdDataStore(user.userId)
                localRepository.saveCurrentUser(user) // Firestore에서 가져온 user를 LocalDataSource에 저장
            }
        return user
    }
}
