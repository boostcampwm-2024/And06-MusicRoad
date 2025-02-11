package com.squirtles.data.repository.remote.firebase

import com.squirtles.domain.datasource.remote.firebase.FirebaseUserDataSource
import com.squirtles.domain.model.User
import com.squirtles.domain.repository.remote.RemoteRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseException
import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository
import javax.inject.Singleton

@Singleton
class FirebaseUserRepositoryImpl(
    private val userDataSource: FirebaseUserDataSource
) : FirebaseUserRepository, RemoteRepository() {

    override suspend fun createGoogleIdUser(
        userId: String,
        userName: String?,
        userProfileImage: String?
    ): Result<User> {
        return handleResult(FirebaseException.CreatedUserFailedException()) {
            userDataSource.createGoogleIdUser(userId, userName, userProfileImage)
        }
    }

    override suspend fun fetchUser(userId: String): Result<User> {
        return handleResult(FirebaseException.UserNotFoundException()) {
            userDataSource.fetchUser(userId)
        }
    }

    override suspend fun updateUserName(userId: String, newUserName: String): Result<Boolean> {
        return handleResult {
            userDataSource.updateUserName(userId, newUserName)
        }
    }
}
