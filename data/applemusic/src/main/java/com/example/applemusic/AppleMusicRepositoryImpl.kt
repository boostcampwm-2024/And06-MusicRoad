package com.example.applemusic

import androidx.paging.PagingData
import com.example.model.MusicVideo
import com.example.model.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppleMusicRepositoryImpl @Inject constructor(
    private val appleMusicDataSource: AppleMusicRemoteDataSource
) : AppleMusicRepository {

    override fun searchSongs(searchText: String): Flow<PagingData<Song>> =
        appleMusicDataSource.searchSongs(searchText)

    override suspend fun searchSongById(songId: String): Result<Song> {
        TODO("Not yet implemented")
    }

    override suspend fun searchMusicVideos(searchText: String): Result<List<MusicVideo>> {
        return handleResult(AppleMusicException.NotFoundException()) {
            appleMusicDataSource.searchMusicVideos(searchText).ifEmpty { null }
        }
    }

    private suspend fun <T> handleResult(
        appleMusicException: AppleMusicException,
        call: suspend () -> T?
    ): Result<T> {
        return runCatching {
            call() ?: throw appleMusicException
        }
    }
}
