package com.squirtles.data.repository

import com.squirtles.domain.firebase.FirebaseRemoteDataSource
import com.squirtles.domain.firebase.FirebaseException
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.User
import com.squirtles.domain.firebase.FirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebaseRepository {

    override suspend fun createGoogleIdUser(userId: String, userName: String?, userProfileImage: String?): Result<User> {
        return handleResult(FirebaseException.CreatedUserFailedException()) {
            firebaseRemoteDataSource.createGoogleIdUser(userId, userName, userProfileImage)
        }
    }

    override suspend fun fetchUser(userId: String): Result<User> {
        return handleResult(FirebaseException.UserNotFoundException()) {
            firebaseRemoteDataSource.fetchUser(userId)
        }
    }

    override suspend fun updateUserName(userId: String, newUserName: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.updateUserName(userId, newUserName)
        }
    }

    override suspend fun fetchPick(pickID: String): Result<Pick> {
        return handleResult(FirebaseException.NoSuchPickException()) {
            firebaseRemoteDataSource.fetchPick(pickID)
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

    override suspend fun fetchMyPicks(userId: String): Result<List<Pick>> {
        return handleResult {
            firebaseRemoteDataSource.fetchMyPicks(userId)
        }
    }

    override suspend fun fetchFavoritePicks(userId: String): Result<List<Pick>> {
        return handleResult {
            firebaseRemoteDataSource.fetchFavoritePicks(userId)
        }
    }

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

    private suspend fun <T> handleResult(
        firebaseRepositoryException: FirebaseException,
        call: suspend () -> T?
    ): Result<T> {
        return runCatching {
            call() ?: throw firebaseRepositoryException
        }
    }

    private suspend fun <T> handleResult(
        call: suspend () -> T
    ): Result<T> {
        return runCatching {
            call()
        }
    }
}
