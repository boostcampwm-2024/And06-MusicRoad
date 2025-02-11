package com.squirtles.domain.repository.remote.firebase

import com.squirtles.domain.model.Pick

interface FirebaseFavoriteRepository {
    // Favorite
    suspend fun fetchIsFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun createFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun deleteFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun fetchFavoritePicks(userId: String): Result<List<Pick>>
}
