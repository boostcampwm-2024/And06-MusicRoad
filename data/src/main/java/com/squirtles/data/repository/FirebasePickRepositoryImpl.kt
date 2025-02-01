package com.squirtles.data.repository

import com.squirtles.domain.model.Pick
import com.squirtles.domain.remote.RemoteRepository
import com.squirtles.domain.remote.firebase.FirebaseException
import com.squirtles.domain.remote.firebase.FirebasePickRepository
import com.squirtles.domain.remote.firebase.FirebaseRemoteDataSource

class FirebasePickRepositoryImpl(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebasePickRepository, RemoteRepository() {

    override suspend fun createPick(pick: Pick): Result<String> {
        return handleResult {
            firebaseRemoteDataSource.createPick(pick)
        }
    }

    override suspend fun deletePick(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.deletePick(pickId, userId)
        }
    }

    override suspend fun fetchPick(pickID: String): Result<Pick> {
        return handleResult(FirebaseException.NoSuchPickException()) {
            firebaseRemoteDataSource.fetchPick(pickID)
        }
    }

    override suspend fun fetchMyPicks(userId: String): Result<List<Pick>> {
        return handleResult {
            firebaseRemoteDataSource.fetchMyPicks(userId)
        }
    }

    override suspend fun fetchPicksInArea(
        lat: Double,
        lng: Double,
        radiusInM: Double
    ): Result<List<Pick>> {
        val pickList = firebaseRemoteDataSource.fetchPicksInArea(lat, lng, radiusInM)
        return handleResult(FirebaseException.NoSuchPickInRadiusException()) {
            pickList.ifEmpty { null }
        }
    }
}
