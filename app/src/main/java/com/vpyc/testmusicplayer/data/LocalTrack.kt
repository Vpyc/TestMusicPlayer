package com.vpyc.testmusicplayer.data

import android.net.Uri
import com.vpyc.testmusicplayer.retrofit.Album
import com.vpyc.testmusicplayer.retrofit.Artist
import com.vpyc.testmusicplayer.retrofit.Track

data class LocalTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri,
    val albumName: String,
    val albumArt: Uri? = null
)

fun LocalTrack.toCommonTrack(): Track {
    return Track(
        id = this.id,
        title = this.title,
        preview = this.uri.toString(),
        duration = this.duration,
        artist = Artist(name = this.artist),
        album = Album(cover = this.albumArt?.toString() ?: "", this.albumName)
    )
}