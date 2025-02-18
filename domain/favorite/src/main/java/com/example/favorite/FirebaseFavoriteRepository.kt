package com.example.favorite

interface FirebaseFavoriteRepository {
    suspend fun fetchIsFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun createFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun deleteFavorite(pickId: String, userId: String): Result<Boolean>
}
