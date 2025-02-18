package com.example.user

import com.example.firebase.FirebaseException
import com.example.firebase.handleResult
import com.example.model.User
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
