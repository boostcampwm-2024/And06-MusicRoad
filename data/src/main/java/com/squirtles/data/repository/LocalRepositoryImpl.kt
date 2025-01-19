package com.squirtles.data.repository

import android.location.Location
import com.squirtles.domain.local.LocalDataSource
import com.squirtles.domain.model.Order
import com.squirtles.domain.model.User
import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
) : LocalRepository {
    override val userIdFromDataStore get() = localDataSource.readUserIdDataStore()
    override val currentUser get() = localDataSource.currentUser
    override val lastLocation get() = localDataSource.lastLocation
    override val favoriteListOrder get() = localDataSource.favoriteListOrder
    override val myListOrder get() = localDataSource.myListOrder

    override suspend fun writeUserIdDataStore(userId: String) {
        localDataSource.writeUserIdDataStore(userId)
    }

    override suspend fun saveCurrentUser(user: User) {
        localDataSource.saveCurrentUser(user)
    }

    override suspend fun saveCurrentLocation(location: Location) {
        localDataSource.saveCurrentLocation(location)
    }

    override suspend fun saveFavoriteListOrder(order: Order) {
        localDataSource.saveFavoriteListOrder(order)
    }

    override suspend fun saveMyListOrder(order: Order) {
        localDataSource.saveMyListOrder(order)
    }
}
