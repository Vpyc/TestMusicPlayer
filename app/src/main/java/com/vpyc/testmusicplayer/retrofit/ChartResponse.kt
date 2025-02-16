package com.vpyc.testmusicplayer.retrofit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ChartResponse(
    val tracks: Tracks
)

data class Tracks(
    val data: List<Track>
)

@Parcelize
data class Track(
    val id: Long,
    val title: String,
    val preview: String,
    val duration: Long,
    val artist: Artist,
    val album: Album,
): Parcelable

@Parcelize
data class Album(
    val cover: String,
    val title: String,
): Parcelable

@Parcelize
data class Artist(
    val name: String,
): Parcelable