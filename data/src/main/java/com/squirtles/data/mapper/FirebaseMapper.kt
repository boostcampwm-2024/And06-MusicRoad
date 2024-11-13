package com.squirtles.data.mapper

import androidx.core.graphics.toColorInt
import com.squirtles.data.datasource.remote.firebase.model.FirebasePick
import com.squirtles.domain.model.Pick
import com.squirtles.domain.model.PickLocation
import com.squirtles.domain.model.Song

internal fun FirebasePick.toPick(): Pick = Pick(
    id = id.toString(),
    song = Song(
        id = songId.toString(),
        songName = songName.toString(),
        artistName = artistName.toString(),
        albumName = albumName.toString(),
        imageUrl = artwork?.get("url") ?: "",
        genreNames = genreNames,
        bgColor = artwork?.get("bgColor")?.let {
            "#${it}".toColorInt()
        } ?: "#000000".toColorInt(),
        externalUrl = externalUrl.toString(),
        previewUrl = previewUrl.toString(),
    ),
    comment = comment.toString(),
    createdAt = createdAt.seconds,
    createdBy = createdBy.toString(),
    location = PickLocation(
        latitude = location?.latitude ?: 0.0,
        longitude = location?.longitude ?: 0.0
    ),
    favoriteCount = favoriteCount,
)
