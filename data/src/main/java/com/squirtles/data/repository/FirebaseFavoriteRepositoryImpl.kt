package com.squirtles.data.repository

import com.squirtles.domain.firebase.FirebaseRemoteDataSource
import com.squirtles.domain.model.Pick
import com.squirtles.domain.remote.RemoteRepository
import com.squirtles.domain.remote.firebase.FirebaseFavoriteRepository


class FirebaseFavoriteRepositoryImpl(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebaseFavoriteRepository, RemoteRepository() {
    override suspend fun fetchIsFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.fetchIsFavorite(pickId, userId)
        }
    }

    override suspend fun createFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.createFavorite(pickId, userId)
        }
    }

    override suspend fun deleteFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.deleteFavorite(pickId, userId)
        }
    }

    override suspend fun fetchFavoritePicks(userId: String): Result<List<Pick>> {
        return handleResult {
            firebaseRemoteDataSource.fetchFavoritePicks(userId)
        }
    }
}
