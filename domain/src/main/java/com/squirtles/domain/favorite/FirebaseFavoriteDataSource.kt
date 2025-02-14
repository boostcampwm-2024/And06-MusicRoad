package com.squirtles.domain.favorite

import com.squirtles.domain.model.Pick

interface FirebaseFavoriteDataSource {
    suspend fun fetchIsFavorite(pickId: String, userId: String): Boolean
    suspend fun createFavorite(pickId: String, userId: String): Boolean
    suspend fun deleteFavorite(pickId: String, userId: String): Boolean
    suspend fun fetchFavoritePicks(userId: String): List<Pick>
}
