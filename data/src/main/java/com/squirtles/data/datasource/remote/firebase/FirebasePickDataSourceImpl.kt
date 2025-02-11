package com.squirtles.data.datasource.remote.firebase

import android.util.Log
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.toObject
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl.Companion.COLLECTION_FAVORITES
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl.Companion.COLLECTION_PICKS
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl.Companion.COLLECTION_USERS
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl.Companion.FIELD_MY_PICKS
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl.Companion.FIELD_PICK_ID
import com.squirtles.data.datasource.remote.firebase.FirebaseDataSourceImpl.Companion.TAG_LOG
import com.squirtles.data.datasource.remote.firebase.model.FirebasePick
import com.squirtles.data.datasource.remote.firebase.model.FirebaseUser
import com.squirtles.data.mapper.toFirebasePick
import com.squirtles.data.mapper.toPick
import com.squirtles.domain.datasource.remote.firebase.FirebasePickDataSource
import com.squirtles.domain.model.Pick
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebasePickDataSourceImpl @Inject constructor(
    private val db: FirebaseFirestore
) : FirebasePickDataSource {

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

    override suspend fun deletePick(pickId: String, userId: String): Boolean {
        val pickDocument = db.collection(COLLECTION_PICKS).document(pickId)
        val userDocument = db.collection(COLLECTION_USERS).document(userId)
        val favoriteDocuments = fetchFavoriteDocuments(pickId)

        return suspendCancellableCoroutine { continuation ->
            db.runTransaction { transaction ->
                transaction.delete(pickDocument)

                favoriteDocuments.forEach { document ->
                    transaction.delete(document)
                }

                transaction.update(userDocument, FIELD_MY_PICKS, FieldValue.arrayRemove(pickId))
            }.addOnSuccessListener { _ ->
                continuation.resume(true)
            }.addOnFailureListener { e ->
                Log.w(TAG_LOG, "Transaction failure.", e)
                continuation.resumeWithException(e)
            }
        }
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

    private fun updateCurrentUserPick(userId: String, pickId: String): Task<Void> {
        val userDoc = db.collection("users").document(userId)
        return userDoc.update("myPicks", FieldValue.arrayUnion(pickId))
    }

    private suspend fun fetchFavoriteDocuments(pickId: String): List<DocumentReference> {
        return suspendCancellableCoroutine { continuation ->
            db.collection(COLLECTION_FAVORITES)
                .whereEqualTo(FIELD_PICK_ID, pickId)
                .get()
                .addOnSuccessListener { querySnapShot ->
                    val documentIds = querySnapShot.documents.map { it.id }
                    val documentRefs = mutableListOf<DocumentReference>()
                    documentIds.forEach { id ->
                        documentRefs.add(db.collection(COLLECTION_FAVORITES).document(id))
                    }
                    continuation.resume(documentRefs)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG_LOG, "Failed to fetch favorite documents id", e)
                    continuation.resumeWithException(e)
                }
        }
    }
}
