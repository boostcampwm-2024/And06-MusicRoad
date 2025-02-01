package com.squirtles.domain.remote.firebase

import com.squirtles.domain.model.User

interface FirebaseUserRepository {
    // user
    suspend fun createUser(): Result<User>
    suspend fun fetchUser(userId: String): Result<User>
    suspend fun updateUserName(userId: String, newUserName: String): Result<Boolean>

    suspend fun <T> handleResult(
        firebaseRepositoryException: FirebaseException,
        call: suspend () -> T?
    ): Result<T> {
        return runCatching {
            call() ?: throw firebaseRepositoryException
        }
    }
}
