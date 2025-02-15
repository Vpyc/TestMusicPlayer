package com.vpyc.testmusicplayer.tracklist

import com.vpyc.testmusicplayer.data.LocalTrack
import com.vpyc.testmusicplayer.data.LocalTracksRepository
import com.vpyc.testmusicplayer.retrofit.Album
import com.vpyc.testmusicplayer.retrofit.Artist
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

private fun LocalTrack.toCommonTrack(): Track {
    return Track(
        id = this.id,
        title = this.title,
        preview = this.uri.toString(),
        duration = this.duration,
        artist = Artist(name = this.artist),
        album = Album(cover = this.albumArt?.toString() ?: "", this.albumName)
    )
}