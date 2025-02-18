package com.example.user

import com.example.model.User

interface FirebaseUserDataSource {
    suspend fun fetchUser(userId: String): User?
    suspend fun createGoogleIdUser(userId: String, userName: String?, userProfileImage: String?): User?
    suspend fun updateUserName(userId: String, newUserName: String): Boolean
}
