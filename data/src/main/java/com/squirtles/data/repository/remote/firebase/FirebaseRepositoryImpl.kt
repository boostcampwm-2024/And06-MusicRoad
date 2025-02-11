package com.squirtles.data.repository.remote.firebase

import com.squirtles.domain.datasource.remote.firebase.FirebaseRemoteDataSource
import com.squirtles.domain.repository.remote.RemoteRepository
import com.squirtles.domain.repository.remote.firebase.FirebaseException
import com.squirtles.domain.repository.remote.firebase.FirebaseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource
) : FirebaseRepository, RemoteRepository() {

    private suspend fun <T> handleResult(
        firebaseRepositoryException: FirebaseException,
        call: suspend () -> T?
    ): Result<T> {
        return runCatching {
            call() ?: throw firebaseRepositoryException
        }
    }
}
