package com.squirtles.domain.datasource.remote.firebase

import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.User

interface FirebaseRemoteDataSource {
    // user
    suspend fun fetchUser(userId: String): User?
    suspend fun createGoogleIdUser(userId: String, userName: String?, userProfileImage: String?): User?
    suspend fun updateUserName(userId: String, newUserName: String): Boolean

    // fetch pick
    suspend fun fetchPick(pickID: String): Pick?
    suspend fun fetchPicksInArea(lat: Double, lng: Double, radiusInM: Double): List<Pick>

    // pick CRUD
    suspend fun createPick(pick: Pick): String
    suspend fun deletePick(pickId: String, userId: String): Boolean

    // pickList
    suspend fun fetchMyPicks(userId: String): List<Pick>
    suspend fun fetchFavoritePicks(userId: String): List<Pick>

    // favorite
    suspend fun fetchIsFavorite(pickId: String, userId: String): Boolean
    suspend fun createFavorite(pickId: String, userId: String): Boolean
    suspend fun deleteFavorite(pickId: String, userId: String): Boolean
//    suspend fun updatePick(pick: Pick)

    companion object {
        protected const val TAG_LOG = "FirebaseDataSourceImpl"

        protected const val COLLECTION_FAVORITES = "favorites"
        protected const val COLLECTION_PICKS = "picks"
        protected const val COLLECTION_USERS = "users"

        protected const val FIELD_PICK_ID = "pickId"
        protected const val FIELD_USER_ID = "userId"
        protected const val FIELD_ADDED_AT = "addedAt"
        protected const val FIELD_MY_PICKS = "myPicks"
    }
}
