package com.squirtles.domain.repository.remote.firebase

import com.squirtles.domain.model.Pick

interface FirebasePickRepository {
    suspend fun createPick(pick: Pick): Result<String>
    suspend fun deletePick(pickId: String, userId: String): Result<Boolean>
    suspend fun fetchPick(pickID: String): Result<Pick>
    suspend fun fetchPicksInArea(lat: Double, lng: Double, radiusInM: Double): Result<List<Pick>>
    suspend fun fetchMyPicks(userId: String): Result<List<Pick>>

    suspend fun <T> handleResult(
        firebaseRepositoryException: FirebaseException,
        call: suspend () -> T?
    ): Result<T> {
        return runCatching {
            call() ?: throw firebaseRepositoryException
        }
    }
}
