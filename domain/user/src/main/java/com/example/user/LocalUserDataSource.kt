package com.example.user

import com.example.model.User
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {
    val currentUser: User?

    fun readUserIdDataStore(): Flow<String?>
    suspend fun saveUserIdDataStore(userId: String)
    suspend fun saveCurrentUser(user: User)
    suspend fun clearUser()
}
