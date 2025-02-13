package com.squirtles.domain.datasource.local

import com.squirtles.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LocalUserDataSource {
    val currentUser: User?

    fun readUserIdDataStore(): Flow<String?>
    suspend fun saveUserIdDataStore(userId: String)
    suspend fun saveCurrentUser(user: User)
    suspend fun clearUser()
}
