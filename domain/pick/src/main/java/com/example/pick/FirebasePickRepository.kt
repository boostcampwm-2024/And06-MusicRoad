package com.example.pick

import com.example.model.Pick

interface FirebasePickRepository {
    suspend fun createPick(pick: Pick): Result<String>
    suspend fun deletePick(pickId: String, userId: String): Result<Boolean>
    suspend fun fetchPick(pickID: String): Result<Pick>
    suspend fun fetchPicksInArea(lat: Double, lng: Double, radiusInM: Double): Result<List<Pick>>
    suspend fun fetchMyPicks(userId: String): Result<List<Pick>>
    suspend fun fetchFavoritePicks(userId: String): Result<List<Pick>>
}
