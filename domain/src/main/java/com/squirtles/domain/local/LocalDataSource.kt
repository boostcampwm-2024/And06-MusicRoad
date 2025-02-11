package com.squirtles.domain.local

import android.location.Location
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocalDataSource {
    val currentUser: User?
    val lastLocation: StateFlow<Location?>
    val favoriteListOrder: Order
    val myListOrder: Order

    fun readUserIdDataStore(): Flow<String?>
    suspend fun saveUserIdDataStore(userId: String)
    suspend fun saveCurrentUser(user: User)
    suspend fun clearUser()
    suspend fun saveCurrentLocation(location: Location)
    suspend fun saveFavoriteListOrder(order: Order)
    suspend fun saveMyListOrder(order: Order)
}
