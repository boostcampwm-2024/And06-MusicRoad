package com.squirtles.domain.applemusic

import androidx.paging.PagingData
import com.squirtles.domain.model.MusicVideo
import com.squirtles.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface AppleMusicRepository {
    fun searchSongs(searchText: String): Flow<PagingData<Song>>
    suspend fun searchSongById(songId: String): Result<Song>
    suspend fun searchMusicVideos(searchText: String): Result<List<MusicVideo>>
}
