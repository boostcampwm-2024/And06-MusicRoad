package com.squirtles.domain.user

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.model.User

interface FirebaseUserRepository : FirebaseRepository {
    // user
    suspend fun createGoogleIdUser(userId: String, userName: String?, userProfileImage: String?): Result<User>
    suspend fun fetchUser(userId: String): Result<User>
    suspend fun updateUserName(userId: String, newUserName: String): Result<Boolean>
}
