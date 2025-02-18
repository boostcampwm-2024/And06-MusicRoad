package com.example.applemusic

import androidx.paging.PagingData
import com.example.model.MusicVideo
import com.example.model.Song
import kotlinx.coroutines.flow.Flow

interface AppleMusicRemoteDataSource {
    fun searchSongs(searchText: String): Flow<PagingData<Song>>
    suspend fun searchMusicVideos(searchText: String): List<MusicVideo>
}
