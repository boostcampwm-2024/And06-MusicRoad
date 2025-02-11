package com.squirtles.domain.usecase.music

import com.squirtles.domain.repository.remote.applemusic.AppleMusicRepository
import javax.inject.Inject

class FetchSongsUseCase @Inject constructor(
    private val appleMusicRepository: AppleMusicRepository
) {
    operator fun invoke(searchText: String) = appleMusicRepository.searchSongs(searchText)
}
