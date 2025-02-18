package com.squirtles.domain.favorite

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.model.Pick

interface FirebaseFavoriteRepository : FirebaseRepository {
    // Favorite
    suspend fun fetchIsFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun createFavorite(pickId: String, userId: String): Result<Boolean>
    suspend fun deleteFavorite(pickId: String, userId: String): Result<Boolean>
}
