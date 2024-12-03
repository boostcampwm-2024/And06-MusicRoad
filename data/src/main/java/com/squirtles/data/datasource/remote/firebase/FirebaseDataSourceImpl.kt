package com.squirtles.data.datasource.remote.firebase

import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.squirtles.data.datasource.remote.firebase.model.FirebaseFavorite
import com.squirtles.data.datasource.remote.firebase.model.FirebasePick
import com.squirtles.data.datasource.remote.firebase.model.FirebaseUser
import com.squirtles.data.mapper.toFirebasePick
import com.squirtles.data.mapper.toPick
import com.squirtles.data.mapper.toUser
import com.squirtles.domain.datasource.FirebaseRemoteDataSource
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.User
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
class FirebaseDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FirebaseRemoteDataSource {

    private val cloudFunctionHelper = CloudFunctionHelper()

    override suspend fun createUser(): User? {
        return suspendCancellableCoroutine { continuation ->
            val randomNum = (1..100).random()
            db.collection("users").add(FirebaseUser("유저$randomNum"))
                .addOnSuccessListener { documentReference ->
                    documentReference.get()
                        .addOnSuccessListener { documentSnapshot ->
                            val savedUser = documentSnapshot.toObject<FirebaseUser>()
                            continuation.resume(
                                savedUser?.toUser()?.copy(userId = documentReference.id)
                            )
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDataSourceImpl", exception.message.toString())
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun fetchUser(userId: String): User? {
        return suspendCancellableCoroutine { continuation ->
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val firebaseUser = document.toObject<FirebaseUser>()
                    continuation.resume(firebaseUser?.toUser()?.copy(userId = userId))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    /**
     * Fetches a pick by ID from Firestore.
     * @param pickID The ID of the pick to fetch.
     * @return  The fetched pick, or null if the pick does not exist on firestore.
     */
    override suspend fun fetchPick(pickID: String): Pick? {
        return suspendCancellableCoroutine { continuation ->
            db.collection("picks").document(pickID).get()
                .addOnSuccessListener { document ->
                    val firestorePick = document.toObject<FirebasePick>()?.copy(id = pickID)
                    val resultPick = firestorePick?.toPick()
                    continuation.resume(resultPick)
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDataSourceImpl", "Failed to fetch a pick", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }

    /**
     * Fetches picks within a given radius from Firestore.
     * @param lat The latitude of the center of the search area.
     * @param lng The longitude of the center of the search area.
     * @param radiusInM The radius in meters of the search area.
     * @return A list of picks within the specified radius, ordered by distance from the center. can be empty.
     */
    override suspend fun fetchPicksInArea(
        lat: Double,
        lng: Double,
        radiusInM: Double
    ): List<Pick> {
        val center = GeoLocation(lat, lng)
        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)

        val queries: MutableList<Query> = ArrayList()
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()
        val matchingPicks: MutableList<Pick> = ArrayList()

        bounds.forEach { bound ->
            val query = db.collection("picks")
                .orderBy("geoHash")
                .startAt(bound.startHash)
                .endAt(bound.endHash)
            queries.add(query)
        }

        try {
            queries.forEach { query ->
                tasks.add(query.get())
            }
            Tasks.whenAllComplete(tasks).await()
        } catch (exception: Exception) {
            Log.e("FirebaseDataSourceImpl", "Failed to fetch picks", exception)
            throw exception
        }

        tasks.forEach { task ->
            val snap = task.result
            snap.documents.forEach { doc ->
                if (isAccurate(doc, center, radiusInM)) {
                    doc.toObject<FirebasePick>()?.run {
                        matchingPicks.add(this.toPick().copy(id = doc.id))
                    }
                }
            }
        }

        return matchingPicks
    }

    /**
     * GeoHash의 FP 문제 - Geohash의 쿼리가 정확하지 않으며 클라이언트 측에서 거짓양성 결과를 필터링해야 합니다.
     * 이러한 추가 읽기로 인해 앱에 비용과 지연 시간이 추가됩니다.
     * @param doc The pick document to check.
     * @param center The center of the search area.
     * @param radiusInM The radius in meters of the search area.
     * @return True if the pick is within the specified radius, false otherwise.
     */
    private fun isAccurate(doc: DocumentSnapshot, center: GeoLocation, radiusInM: Double): Boolean {
        val location = doc.getGeoPoint("location") ?: return false

        val docLocation = GeoLocation(location.latitude, location.longitude)
        val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)

        return distanceInM <= radiusInM
    }

    /**
     * Creates a new pick in Firestore.
     * @param pick The pick to create.
     * @return The created pick.
     */
    override suspend fun createPick(pick: Pick): String =
        suspendCancellableCoroutine { continuation ->
            val firebasePick = pick.toFirebasePick()

            // add() 메소드는 Cloud Firestore에서 ID를 자동으로 생성
            db.collection("picks").add(firebasePick)
                .addOnSuccessListener { documentReference ->
                    val pickId = documentReference.id
                    // 유저의 픽 정보 업데이트
                    updateCurrentUserPick(pick.createdBy.userId, pickId)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(pickId)
                            } else {
                                continuation.resumeWithException(
                                    task.exception ?: Exception("Failed to updating user pick info")
                                )
                            }
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDataSourceImpl", "Failed to create a pick", exception)
                    continuation.resumeWithException(exception)
                }
        }

    override suspend fun deletePick(pickId: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            db.collection("picks").document(pickId)
                .delete()
                .addOnSuccessListener {
                    continuation.resume(true)
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDataSourceImpl", "Failed to delete pick", exception)
                    continuation.resumeWithException(exception)
                }
        }
        // TODO: 유저가 등록한 리스트에서 이 픽 id를 삭제
        // TODO: favorite에서 이 픽 id 삭제
    }

    override suspend fun fetchMyPicks(userId: String): List<Pick> {
        val userDocument = fetchUserDocument(userId)
        if (userDocument.exists().not()) throw Exception("No user info in database")

        val tasks = mutableListOf<Task<DocumentSnapshot>>()
        val myPicks = mutableListOf<Pick>()

        try {
            userDocument.toObject<FirebaseUser>()?.myPicks?.forEach { pickId ->
                tasks.add(
                    db.collection(COLLECTION_PICKS)
                        .document(pickId)
                        .get()
                )
            }
            Tasks.whenAllComplete(tasks).await()
        } catch (exception: Exception) {
            Log.e("FirebaseDataSourceImpl", "Failed to fetch my picks", exception)
            throw exception
        }

        tasks.forEach { task ->
            task.result.toObject<FirebasePick>()?.run {
                myPicks.add(this.toPick().copy(id = task.result.id))
            }
        }

        return myPicks.reversed()
    }

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

    private fun updateCurrentUserPick(userId: String, pickId: String): Task<Void> {
        val userDoc = db.collection("users").document(userId)
        return userDoc.update("myPicks", FieldValue.arrayUnion(pickId))
    }

    private suspend fun fetchFavoriteByPickIdAndUserId(
        pickId: String,
        userId: String
    ): QuerySnapshot {
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
        return suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_FAVORITES)
                .whereEqualTo(FIELD_USER_ID, userId)
                .orderBy(FIELD_ADDED_AT, Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    continuation.resume(result)
                }
                .addOnFailureListener { exception ->
                    Log.w(
                        "FirebaseDataSourceImpl",
                        "Error at fetching favorite documents",
                        exception
                    )
                    continuation.resumeWithException(exception)
                }
        }
    }

    private suspend fun fetchUserDocument(userId: String): DocumentSnapshot {
        return suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_USERS).document(userId)
                .get()
                .addOnSuccessListener { document ->
                    continuation.resume(document)
                }
                .addOnFailureListener { exception ->
                    Log.e("FirebaseDataSourceImpl", "Failed to get user document", exception)
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

    companion object {
        private const val COLLECTION_FAVORITES = "favorites"
        private const val COLLECTION_PICKS = "picks"
        private const val COLLECTION_USERS = "users"

        private const val FIELD_PICK_ID = "pickId"
        private const val FIELD_USER_ID = "userId"
        private const val FIELD_ADDED_AT = "addedAt"
    }
}
