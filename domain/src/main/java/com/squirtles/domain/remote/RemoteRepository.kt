package com.squirtles.domain.remote

abstract class RemoteRepository {
    suspend fun <T> handleResult(
        call: suspend () -> T
    ): Result<T> {
        return runCatching {
            call()
        }
    }
}
