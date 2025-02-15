package com.vpyc.testmusicplayer.data

import android.net.Uri

data class LocalTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri,
    val albumName: String,
    val albumArt: Uri? = null
)