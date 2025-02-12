package com.vpyc.testmusicplayer.tracklist

import com.vpyc.testmusicplayer.retrofit.Track

sealed class TrackListState {
    data object Loading : TrackListState()
    data class Success(val tracks: List<Track>) : TrackListState()
    data class Error(val message: String) : TrackListState()
}

sealed class TrackListIntent {
    data object LoadTracks : TrackListIntent()
}