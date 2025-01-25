package com.squirtles.domain.repository

import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.User

interface FirebaseRepository {
    suspend fun createUser(): Result<User>
    suspend fun createGoogledIdUser(userId: String, userName: String?, userProfileImage: String?): Result<User>

    suspend fun fetchUser(userId: String): Result<User>
    suspend fun updateUserName(userId: String, newUserName: String): Result<Boolean>

    suspend fun fetchPick(pickID: String): Result<Pick>
    suspend fun fetchPicksInArea(lat: Double, lng: Double, radiusInM: Double): Result<List<Pick>>
    suspend fun createPick(pick: Pick): Result<String>
    suspend fun deletePick(pickId: String, userId: String): Result<Boolean>

    suspend fun fetchMyPicks(userId: String): Result<List<Pick>>
    suspend fun fetchFavoritePicks(userId: String): Result<List<Pick>>
    suspend fun fetchIsFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun createFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun deleteFavorite(pickId: String, userId: String): Result<Boolean>
}
