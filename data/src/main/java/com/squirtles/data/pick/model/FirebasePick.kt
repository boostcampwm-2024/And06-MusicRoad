package com.squirtles.data.pick.model

import androidx.core.graphics.toColorInt
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ServerTimestamp
import com.squirtles.domain.model.Creator
import com.squirtles.domain.model.LocationPoint
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.Song
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Firestore에 저장된 pick document를 불러와 변환하기위한 데이터 클래스
 */
data class FirebasePick(
    val id: String? = null,
    val albumName: String? = null,
    val artistName: String? = null,
    val artwork: Map<String, String>? = null,
    val comment: String? = null,
    @ServerTimestamp val createdAt: Timestamp? = null, // 등록 시 자동으로 서버 시간으로 설정되도록 합니다
    val createdBy: Map<String, String>? = null,
    val externalUrl: String? = null,
    val favoriteCount: Int = 0,
    val genreNames: List<String> = emptyList(),
    val geoHash: String? = null,
    val location: GeoPoint? = null,
    val previewUrl: String? = null,
    val musicVideoUrl: String? = null,
    val musicVideoThumbnail: String? = null,
    val songId: String? = null,
    val songName: String? = null,
)
