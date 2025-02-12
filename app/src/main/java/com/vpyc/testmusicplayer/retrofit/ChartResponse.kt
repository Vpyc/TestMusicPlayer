package com.vpyc.testmusicplayer.retrofit

data class ChartResponse(
    val total: Int,
    val tracks: Tracks
)

data class Tracks(
    val data: List<Track>
)

data class Track(
    val id: Long,
    val title: String,
    val duration: Int,
    val artist: Artist,
    val album: Album
)

data class Album(
    val id: Int,
    val cover: String,
)

data class Artist(
    val id: Int,
    val name: String,
)