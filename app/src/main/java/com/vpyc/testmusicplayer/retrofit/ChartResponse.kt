package com.vpyc.testmusicplayer.retrofit

data class ChartResponse(
    val tracks: Tracks
)

data class Tracks(
    val data: List<Track>
)

data class Track(
    val id: Long,
    val title: String,
    val preview: String,
    val duration: Int,
    val artist: Artist,
    val album: Album,
)

data class Album(
    val cover: String,
    val title: String,
)

data class Artist(
    val name: String,
)