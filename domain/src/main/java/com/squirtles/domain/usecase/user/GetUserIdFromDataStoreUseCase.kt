package com.squirtles.domain.usecase.user

import android.util.Log
import com.squirtles.domain.local.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUserIdFromDataStoreUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke() : String? {
        return try {
            val userId = localRepository.readUserIdDataStore().first()
            if (userId.isNullOrBlank()) null else userId
        } catch (e: Exception) {
            Log.e("GetUserIdUseCase", "Error reading DataStore", e)
            null
        }
    }
}
