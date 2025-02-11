package com.squirtles.data.repository.remote.firebase

import com.squirtles.domain.datasource.remote.firebase.FirebaseFavoriteDataSource
import com.squirtles.domain.model.Pick
import com.squirtles.domain.repository.remote.RemoteRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseFavoriteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseFavoriteRepositoryImpl @Inject constructor(
    private val favoriteDataSource: FirebaseFavoriteDataSource
) : FirebaseFavoriteRepository, RemoteRepository() {

    override suspend fun fetchIsFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            favoriteDataSource.fetchIsFavorite(pickId, userId)
        }
    }

    override suspend fun createFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            favoriteDataSource.createFavorite(pickId, userId)
        }
    }

    override suspend fun deleteFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            favoriteDataSource.deleteFavorite(pickId, userId)
        }
    }

    override suspend fun fetchFavoritePicks(userId: String): Result<List<Pick>> {
        return handleResult {
            favoriteDataSource.fetchFavoritePicks(userId)
        }
    }
}
