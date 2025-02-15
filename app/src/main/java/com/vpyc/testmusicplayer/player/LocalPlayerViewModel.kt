package com.vpyc.testmusicplayer.player

import androidx.media3.exoplayer.ExoPlayer
import com.vpyc.testmusicplayer.data.LocalTracksRepository
import com.vpyc.testmusicplayer.data.toCommonTrack
import com.vpyc.testmusicplayer.retrofit.Track
import javax.inject.Inject

class LocalPlayerViewModel @Inject constructor(
    private val repository: LocalTracksRepository,
    exoPlayer: ExoPlayer
) : BasePlayerViewModel(exoPlayer) {

    override suspend fun fetchTrack(trackId: Long): Track? {
        return repository.getTrackById(trackId)?.toCommonTrack()
    }
}