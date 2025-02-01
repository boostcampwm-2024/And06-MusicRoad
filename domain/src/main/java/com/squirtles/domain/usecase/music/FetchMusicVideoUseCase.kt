package com.squirtles.domain.usecase.music

import com.squirtles.domain.model.MusicVideo
import com.squirtles.domain.model.Song
import com.squirtles.domain.applemusic.AppleMusicRepository
import javax.inject.Inject

class FetchMusicVideoUseCase @Inject constructor(
    private val appleMusicRepository: AppleMusicRepository
) {
    suspend operator fun invoke(song: Song): MusicVideo? {
        val keyword = "${song.songName}-${song.artistName}"
        appleMusicRepository.searchMusicVideos(keyword).onSuccess { musicVideos ->
            return musicVideos.find {
                it.artistName == song.artistName && song.songName in it.songName
            }
        }
        return null
    }
}
