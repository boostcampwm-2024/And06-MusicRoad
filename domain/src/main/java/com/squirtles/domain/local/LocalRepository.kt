package com.squirtles.domain.local

import android.location.Location
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LocalRepository {
    val userIdFromDataStore: Flow<String?> // 기기에 저장된 userId
    val currentUser: User
    val lastLocation: StateFlow<Location?>
    val favoriteListOrder: Order // 픽 보관함 정렬 순서
    val myListOrder: Order // 등록한 픽 정렬 순서

    suspend fun writeUserIdDataStore(userId: String)
    suspend fun saveCurrentUser(user: User)
    suspend fun saveCurrentLocation(location: Location)
    suspend fun saveFavoriteListOrder(order: Order)
    suspend fun saveMyListOrder(order: Order)
}
