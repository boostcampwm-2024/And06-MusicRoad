package com.squirtles.data.repository.remote.firebase

import com.squirtles.domain.datasource.remote.firebase.FirebaseRemoteDataSource
import com.squirtles.domain.model.User
import com.squirtles.domain.repository.remote.RemoteRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseException
import com.squirtles.domain.repository.remote.firebase.FirebaseUserRepository

class FirebaseUserRepositoryImpl(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebaseUserRepository, RemoteRepository() {

    override suspend fun createGoogleIdUser(
        userId: String,
        userName: String?,
        userProfileImage: String?
    ): Result<User> {
        return handleResult(FirebaseException.CreatedUserFailedException()) {
            firebaseRemoteDataSource.createGoogleIdUser(userId, userName, userProfileImage)
        }
    }

    override suspend fun fetchUser(userId: String): Result<User> {
        return handleResult(FirebaseException.UserNotFoundException()) {
            firebaseRemoteDataSource.fetchUser(userId)
        }
    }

    override suspend fun updateUserName(userId: String, newUserName: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.updateUserName(userId, newUserName)
        }
    }
}
