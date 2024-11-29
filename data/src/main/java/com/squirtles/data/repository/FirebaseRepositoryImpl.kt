package com.squirtles.data.repository

import com.squirtles.domain.datasource.FirebaseRemoteDataSource
import com.squirtles.domain.exception.FirebaseException
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.User
import com.squirtles.domain.repository.FirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebaseRepository {

    override suspend fun createUser(): Result<User> {
        return handleResult(FirebaseException.CreatedUserFailedException()) {
            firebaseRemoteDataSource.createUser()
        }
    }

    override suspend fun fetchUser(userId: String): Result<User> {
        return handleResult(FirebaseException.UserNotFoundException()) {
            firebaseRemoteDataSource.fetchUser(userId)
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

    override suspend fun deletePick(pickId: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.deletePick(pickId)
        }
    }

    override suspend fun createFavorite(pickId: String, userId: String): Result<Boolean> {
        return handleResult {
            firebaseRemoteDataSource.createFavorite(pickId, userId)
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
