package com.example.applemusic.usecase

import com.example.applemusic.AppleMusicRepository
import javax.inject.Inject

class FetchSongsUseCase @Inject constructor(
    private val appleMusicRepository: AppleMusicRepository
) {
    operator fun invoke(searchText: String) = appleMusicRepository.searchSongs(searchText)
}
