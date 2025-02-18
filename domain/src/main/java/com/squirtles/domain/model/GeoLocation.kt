package com.squirtles.domain.model

data class GeoLocation(
    val latitude: Double,
    val longitude: Double,
) {
    init {
        require(latitude in -90.0..90.0) { "latitude must be in -90.0 ~ 90.0 range" }
        require(longitude in -180.0..180.0) { "longitude must be in -180.0 ~ 180.0 range" }
    }
}
