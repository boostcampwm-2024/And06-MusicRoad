package com.squirtles.musicroad.map

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.overlay.Marker
import com.squirtles.domain.model.Pick
import com.squirtles.domain.usecase.FetchLastLocationUseCase
import com.squirtles.domain.usecase.FetchPickInAreaUseCase
import com.squirtles.domain.usecase.GetCurrentUserUseCase
import com.squirtles.domain.usecase.SaveLastLocationUseCase
import com.squirtles.musicroad.map.marker.MarkerKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MarkerState(
    val prevClickedMarker: Marker? = null, // 이전에 클릭한 마커(클러스터 마커 & 단말 마커)
    val clusterPickList: List<Pick>? = null, // 클러스터 마커의 픽 정보
    val curPickId: String? = null // 현재 선택한 마커의 pick id
)

@HiltViewModel
class MapViewModel @Inject constructor(
    fetchLastLocationUseCase: FetchLastLocationUseCase,
    private val saveLastLocationUseCase: SaveLastLocationUseCase,
    private val fetchPickInAreaUseCase: FetchPickInAreaUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private var _lastCameraPosition: CameraPosition? = null
    val lastCameraPosition get() = _lastCameraPosition

    private val _picks: MutableMap<String, Pick> = mutableMapOf() // key: pickId, value: Pick
    val picks: Map<String, Pick> get() = _picks

    private val _nearPicks = MutableStateFlow<List<Pick>>(emptyList())
    val nearPicks = _nearPicks.asStateFlow()

    private val _clickedMarkerState = MutableStateFlow(MarkerState())
    val clickedMarkerState = _clickedMarkerState.asStateFlow()

    // FIXME : 네이버맵의 LocationChangeListener에서 실시간으로 변하는 위치 정보 -> 더 나은 방법이 있으면 고쳐주세요
    private var _currentLocation: Location? = null
    val curLocation get() = _currentLocation

    // LocalDataSource에 저장되는 위치 정보
    // Firestore 데이터 쿼리 작업 최소화 및 위치데이터 공유 용도
    val lastLocation: StateFlow<Location?> = fetchLastLocationUseCase()

    fun getUserId() = getCurrentUserUseCase().userId

    fun setLastCameraPosition(cameraPosition: CameraPosition) {
        _lastCameraPosition = cameraPosition
    }

    fun updateCurLocation(location: Location) {
        _currentLocation = location

        if (lastLocation.value == null
            || calculateDistance(location.latitude, location.longitude) > 5.0
        ) {
            saveCurLocation(location)
        }
    }

    private fun saveCurLocation(location: Location) {
        viewModelScope.launch {
            saveLastLocationUseCase(location)
        }
    }

    fun saveCurLocationForced() {
        _currentLocation?.let { location ->
            saveCurLocation(location)
        }
    }

    fun calculateDistance(
        lat: Double,
        lng: Double,
        from: Location? = lastLocation.value,
    ): Double {
        return from?.let {
            val location = Location("pickLocation").apply {
                latitude = lat
                longitude = lng
            }
            from.distanceTo(location).toDouble()
        } ?: -1.0
    }

    fun setClickedMarker(context: Context, marker: Marker) {
        viewModelScope.launch {
            marker.toggleSizeByClick(context, true)
            _clickedMarkerState.emit(_clickedMarkerState.value.copy(prevClickedMarker = marker))
        }
    }

    fun setClickedMarkerState(
        context: Context,
        marker: Marker,
        clusterTag: String? = null,
        pickId: String? = null
    ) {
        viewModelScope.launch {
            val prevClickedMarker = _clickedMarkerState.value.prevClickedMarker
            if (prevClickedMarker == marker) return@launch

            prevClickedMarker?.toggleSizeByClick(context, false)
            marker.toggleSizeByClick(context, true)
            val pickList = clusterTag?.split(",")?.mapNotNull { id -> picks[id] }
            _clickedMarkerState.emit(MarkerState(marker, pickList, pickId))
        }
    }

    fun resetClickedMarkerState(context: Context) {
        viewModelScope.launch {
            val prevClickedMarker = _clickedMarkerState.value.prevClickedMarker
            prevClickedMarker?.toggleSizeByClick(context, false)
            _clickedMarkerState.emit(MarkerState(null, null, null))
        }
    }

    fun fetchPicksInBounds(leftTop: LatLng, rightBottom: LatLng, clusterer: Clusterer<MarkerKey>?) {
        viewModelScope.launch {
            val center = LatLng(
                (leftTop.latitude + rightBottom.latitude) / 2,
                (leftTop.longitude + rightBottom.longitude) / 2
            )
            val radiusInM = leftTop.distanceTo(rightBottom)
            val fetchPicks = fetchPickInAreaUseCase(center.latitude, center.longitude, radiusInM)

            fetchPicks.onSuccess { pickList ->
                val newKeyTagMap: MutableMap<MarkerKey, String> = mutableMapOf()
                Log.d("MapViewModel", "$pickList")
                pickList.forEach { pick ->
                    newKeyTagMap[MarkerKey(pick)] = pick.id
                    _picks[pick.id] = pick
                }
                clusterer?.addAll(newKeyTagMap)
            }
            fetchPicks.onFailure {
                // TODO: NoSuchPickInRadiusException일 때
            }
        }
    }

    fun requestPickNotificationArea(location: Location, notiRadius: Double) {
        viewModelScope.launch {
            fetchPickInAreaUseCase(location.latitude, location.longitude, notiRadius)
                .onSuccess {
                    _nearPicks.emit(it)
                }.onFailure {
                    _nearPicks.emit(emptyList())
                }
        }
    }

    private fun Marker.toggleSizeByClick(context: Context, isClicked: Boolean) {
        val defaultIconWidth = this.icon.getIntrinsicWidth(context)
        val defaultIconHeight = this.icon.getIntrinsicHeight(context)

        this.width =
            if (isClicked) (defaultIconWidth * MARKER_SCALE).toInt() else defaultIconWidth
        this.height =
            if (isClicked) (defaultIconHeight * MARKER_SCALE).toInt() else defaultIconHeight
    }

    companion object {
        private const val MARKER_SCALE = 1.5
    }
}
