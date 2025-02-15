package com.vpyc.testmusicplayer.tracklist

import com.vpyc.testmusicplayer.data.toCommonTrack
import com.vpyc.testmusicplayer.data.LocalTracksRepository
import com.vpyc.testmusicplayer.retrofit.Track
import javax.inject.Inject

class LocalTrackListViewModel @Inject constructor(
    private val localTracksRepository: LocalTracksRepository
) : BaseTrackListViewModel() {

    override suspend fun getTracks(query: String): List<Track>? {
        return if (query.isBlank()) {
            localTracksRepository.getLocalTracks().map { it.toCommonTrack() }
        } else {
            localTracksRepository.searchLocalTracks(query).map { it.toCommonTrack() }
        }
    }

}