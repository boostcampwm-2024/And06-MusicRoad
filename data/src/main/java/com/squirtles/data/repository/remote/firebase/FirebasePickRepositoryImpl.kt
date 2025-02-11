package com.squirtles.data.repository.remote.firebase

import com.squirtles.domain.datasource.remote.firebase.FirebasePickDataSource
import com.squirtles.domain.model.Pick
import com.squirtles.domain.repository.remote.RemoteRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseException
import com.squirtles.domain.repository.remote.firebase.FirebasePickRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebasePickRepositoryImpl @Inject constructor(
    private val pickDataSource: FirebasePickDataSource
) : FirebasePickRepository, RemoteRepository() {

    override suspend fun createPick(pick: Pick): Result<String> {
        return handleResult {
            pickDataSource.createPick(pick)
        }
    }

    override suspend fun deletePick(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            pickDataSource.deletePick(pickId, userId)
        }
    }

    override suspend fun fetchPick(pickID: String): Result<Pick> {
        return handleResult(FirebaseException.NoSuchPickException()) {
            pickDataSource.fetchPick(pickID)
        }
    }

    override suspend fun fetchMyPicks(userId: String): Result<List<Pick>> {
        return handleResult {
            pickDataSource.fetchMyPicks(userId)
        }
    }

    override suspend fun fetchPicksInArea(
        lat: Double,
        lng: Double,
        radiusInM: Double
    ): Result<List<Pick>> {
        val pickList = pickDataSource.fetchPicksInArea(lat, lng, radiusInM)
        return handleResult(FirebaseException.NoSuchPickInRadiusException()) {
            pickList.ifEmpty { null }
        }
    }
}
