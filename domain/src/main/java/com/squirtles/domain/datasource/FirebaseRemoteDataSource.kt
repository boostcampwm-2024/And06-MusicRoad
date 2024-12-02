package com.squirtles.domain.datasource

import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.User

interface FirebaseRemoteDataSource {
    suspend fun createUser(): User?
    suspend fun fetchUser(userId: String): User?
    suspend fun updateUserName(userId: String, newUserName: String): Boolean

    suspend fun fetchPick(pickID: String): Pick?
    suspend fun fetchPicksInArea(lat: Double, lng: Double, radiusInM: Double): List<Pick>
    suspend fun createPick(pick: Pick): String
    suspend fun deletePick(pickId: String): Boolean

    suspend fun fetchMyPicks(userId: String): List<Pick>
    suspend fun fetchFavoritePicks(userId: String): List<Pick>
    suspend fun fetchIsFavorite(pickId: String, userId: String): Boolean
    suspend fun createFavorite(pickId: String, userId: String): Boolean
    suspend fun deleteFavorite(pickId: String, userId: String): Boolean
//    suspend fun updatePick(pick: Pick)
}
