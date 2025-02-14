package com.squirtles.data.user

import com.squirtles.domain.user.FirebaseUserDataSource
import com.squirtles.domain.model.User
import com.squirtles.domain.firebase.FirebaseException
import com.squirtles.domain.user.FirebaseUserRepository
import javax.inject.Singleton

@Singleton
class FirebaseUserRepositoryImpl(
    private val userDataSource: FirebaseUserDataSource
) : FirebaseUserRepository {

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
