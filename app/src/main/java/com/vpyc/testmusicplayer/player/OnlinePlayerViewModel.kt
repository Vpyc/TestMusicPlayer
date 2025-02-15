package com.vpyc.testmusicplayer.player

import androidx.media3.exoplayer.ExoPlayer
import com.vpyc.testmusicplayer.data.TrackRepository
import com.vpyc.testmusicplayer.retrofit.Track
import javax.inject.Inject

class OnlinePlayerViewModel @Inject constructor(
    private val repository: TrackRepository,
    exoPlayer: ExoPlayer
) : BasePlayerViewModel(exoPlayer) {
    override suspend fun fetchTrack(trackId: Long): Track? {
        return repository.getTrackById(trackId)
    }
}