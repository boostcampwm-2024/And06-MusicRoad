package com.example.pick

import com.example.model.Pick

interface FirebasePickDataSource {
    suspend fun createPick(pick: Pick): String
    suspend fun deletePick(pickId: String, userId: String): Boolean
    suspend fun fetchPick(pickID: String): Pick?
    suspend fun fetchPicksInArea(lat: Double, lng: Double, radiusInM: Double): List<Pick>
    suspend fun fetchMyPicks(userId: String): List<Pick>
    suspend fun fetchFavoritePicks(userId: String): List<Pick>
}
