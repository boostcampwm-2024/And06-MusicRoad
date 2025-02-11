package com.squirtles.data.datasource.remote.firebase

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.squirtles.data.datasource.remote.firebase.model.FirebaseFavorite
import com.squirtles.data.datasource.remote.firebase.model.FirebasePick
import com.squirtles.data.mapper.toPick
import com.squirtles.domain.datasource.remote.firebase.FirebaseDataSourceConstants.COLLECTION_FAVORITES
import com.squirtles.domain.datasource.remote.firebase.FirebaseDataSourceConstants.COLLECTION_PICKS
import com.squirtles.domain.datasource.remote.firebase.FirebaseDataSourceConstants.FIELD_ADDED_AT
import com.squirtles.domain.datasource.remote.firebase.FirebaseDataSourceConstants.FIELD_PICK_ID
import com.squirtles.domain.datasource.remote.firebase.FirebaseDataSourceConstants.FIELD_USER_ID
import com.squirtles.domain.datasource.remote.firebase.FirebaseFavoriteDataSource
import com.squirtles.domain.model.Pick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebaseFavoriteDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val cloudFunctionHelper: CloudFunctionHelper
) : FirebaseFavoriteDataSource {

    override suspend fun fetchFavoritePicks(userId: String): List<Pick> {
        val favoriteDocuments = fetchFavoritesByUserId(userId)

        val tasks = mutableListOf<Task<DocumentSnapshot>>()
        val favorites = mutableListOf<Pick>()

        try {
            favoriteDocuments.forEach { doc ->
                tasks.add(
                    db.collection(COLLECTION_PICKS)
                        .document(doc.data[FIELD_PICK_ID].toString())
                        .get()
                )
            }
            Tasks.whenAllComplete(tasks).await()
        } catch (exception: Exception) {
            Log.e("FirebaseDataSourceImpl", "Failed to get favorite picks", exception)
            throw exception
        }
        tasks.forEach { task ->
            task.result.toObject<FirebasePick>()?.run {
                favorites.add(this.toPick().copy(id = task.result.id))
            }
        }

        return favorites
    }

    override suspend fun fetchIsFavorite(pickId: String, userId: String): Boolean {
        val favoriteDocument = fetchFavoriteByPickIdAndUserId(pickId, userId)
        return favoriteDocument.isEmpty.not()
    }

    override suspend fun createFavorite(pickId: String, userId: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val firebaseFavorite = FirebaseFavorite(
                pickId = pickId,
                userId = userId
            )

            db.collection(COLLECTION_FAVORITES)
                .add(firebaseFavorite)
                .addOnSuccessListener {
                    // favorites에 문서 생성 후 클라우드 함수가 완료됐을 때 담기 완료
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            updateFavoriteCount(pickId) // 클라우드 함수 호출
                            continuation.resume(true)
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDataSourceImpl", "Failed to create favorite", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun deleteFavorite(pickId: String, userId: String): Boolean {
        val favoriteDocument = fetchFavoriteByPickIdAndUserId(pickId, userId)
        return suspendCancellableCoroutine { continuation ->
            favoriteDocument.forEach { document ->
                db.collection(COLLECTION_FAVORITES).document(document.id)
                    .delete()
                    .addOnSuccessListener {
                        // favorites에 문서 삭제 후 클라우드 함수가 완료됐을 때 담기 해제 완료
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                updateFavoriteCount(pickId) // 클라우드 함수 호출
                                continuation.resume(true)
                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(
                            "FirebaseDataSourceImpl",
                            "Error deleting favorite document",
                            exception
                        )
                        continuation.resumeWithException(exception)
                    }
            }
        }
    }

    private suspend fun fetchFavoriteByPickIdAndUserId(pickId: String, userId: String): QuerySnapshot {
        return suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_FAVORITES)
                .whereEqualTo(FIELD_PICK_ID, pickId)
                .whereEqualTo(FIELD_USER_ID, userId)
                .get()
                .addOnSuccessListener { result ->
                    continuation.resume(result)
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "FirebaseDataSourceImpl",
                        "Error at fetching favorite document",
                        exception
                    )
                    continuation.resumeWithException(exception)
                }
        }
    }

    private suspend fun fetchFavoritesByUserId(userId: String): QuerySnapshot {
        val query = db.collection(COLLECTION_FAVORITES)
            .whereEqualTo(FIELD_USER_ID, userId)
            .orderBy(FIELD_ADDED_AT, Query.Direction.DESCENDING)

        return executeQuery(query)
    }

    private suspend fun executeQuery(query: Query): QuerySnapshot {
        return suspendCancellableCoroutine { continuation ->
            query.get()
                .addOnSuccessListener { result ->
                    continuation.resume(result)
                }
                .addOnFailureListener { exception ->
                    Log.w("FirebaseDataSourceImpl", "Error fetching favorite documents", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }

    private suspend fun updateFavoriteCount(pickId: String) {
        try {
            val result = cloudFunctionHelper.updateFavoriteCount(pickId)
            result.onSuccess {
                Log.d("FirebaseDataSourceImpl", "Success to update favorite count")
            }.onFailure { exception ->
                Log.e("FirebaseDataSourceImpl", "Failed to update favorite count", exception)
                throw exception
            }
        } catch (e: Exception) {
            Log.e("FirebaseDataSourceImpl", "Exception occurred while updating favorite count", e)
            throw e
        }
    }
}
