package com.squirtles.data.datasource.remote.applemusic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.squirtles.data.datasource.remote.applemusic.SearchSongsPagingSource.Companion.SEARCH_PAGE_SIZE
import com.squirtles.data.datasource.remote.applemusic.api.AppleMusicApi
import com.squirtles.data.datasource.remote.applemusic.model.SearchResponse
import com.squirtles.data.mapper.toMusicVideo
import com.squirtles.domain.applemusic.AppleMusicRemoteDataSource
import com.squirtles.domain.model.MusicVideo
import com.squirtles.domain.model.Song
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class AppleMusicDataSourceImpl @Inject constructor(
    private val appleMusicApi: AppleMusicApi
) : AppleMusicRemoteDataSource {

    override fun searchSongs(searchText: String): Flow<PagingData<Song>> {
        return Pager(
            config = PagingConfig(
                pageSize = SEARCH_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchSongsPagingSource(appleMusicApi, searchText) }
        ).flow
    }

    override suspend fun searchSongById(songId: String): Song {
        TODO("Not yet implemented")
    }

    override suspend fun searchMusicVideos(searchText: String): List<MusicVideo> {
        val searchResult = requestSearchApi(searchText, "music-videos")
        return searchResult.results.musicVideos?.data?.map {
            it.toMusicVideo()
        } ?: emptyList()
    }

    private suspend fun requestSearchApi(searchText: String, types: String): SearchResponse {
        return checkResponse(
            appleMusicApi.searchSongs(
                storefront = DEFAULT_STOREFRONT,
                types = types,
                term = searchText,
                limit = 10,
                offset = "0",
            )
        )
    }

    private fun <T> checkResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return requireNotNull(response.body())
        } else {
            val errorBody = requireNotNull(response.errorBody()?.string())
            throw Exception(errorBody)
        }
    }

    companion object {
        const val DEFAULT_STOREFRONT = "kr"
    }
}
