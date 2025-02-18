package com.example.favorite

import com.example.model.Pick

interface FirebaseFavoriteDataSource {
    suspend fun fetchIsFavorite(pickId: String, userId: String): Boolean
    suspend fun createFavorite(pickId: String, userId: String): Boolean
    suspend fun deleteFavorite(pickId: String, userId: String): Boolean
}
