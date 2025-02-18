package com.example.applemusic.usecase

import com.example.applemusic.AppleMusicRepository
import com.example.model.MusicVideo
import com.example.model.Song
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
