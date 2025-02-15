package com.vpyc.testmusicplayer.player

import com.vpyc.testmusicplayer.retrofit.Track

data class PlayerUiState(
    val track: Track?,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L
)

sealed class PlayerIntent {
    data class LoadTrackWithContext(
        val trackIds: List<Long>,
        val currentTrackId: Long
    ) : PlayerIntent()
    object PlayPause : PlayerIntent()
    object Next : PlayerIntent()
    object Previous : PlayerIntent()
    data class Seek(val position: Long) : PlayerIntent()
}