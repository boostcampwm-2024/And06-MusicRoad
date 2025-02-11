package com.squirtles.domain.applemusic

import androidx.paging.PagingData
import com.squirtles.domain.model.MusicVideo
import com.squirtles.domain.model.Song
import kotlinx.coroutines.flow.Flow

interface AppleMusicRemoteDataSource {
    fun searchSongs(searchText: String): Flow<PagingData<Song>>
    suspend fun searchSongById(songId: String): Song
    suspend fun searchMusicVideos(searchText: String): List<MusicVideo>
}
