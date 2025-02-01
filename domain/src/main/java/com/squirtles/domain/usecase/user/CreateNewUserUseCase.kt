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
        val createdUser = firebaseRepository.createUser() // Firebase에 유저 생성
            .onSuccess { user ->
                localRepository.writeUserIdDataStore(user.userId) // 생성된 유저의 userId를 DataStore에 저장 후 user 반환
                localRepository.saveCurrentUser(user) // 생성된 유저를 LocalDataSource currentUser 에 저장
            }
        return createdUser
    }
}
