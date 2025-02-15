package com.vpyc.testmusicplayer.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(
    private val context: Context
) {

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID
    )

    fun queryTracks(
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): List<LocalTrack> {
        val tracks = mutableListOf<LocalTrack>()

        val cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            tracks.addAll(parseCursor(it))
        }

        return tracks
    }

    private fun parseCursor(cursor: Cursor): List<LocalTrack> {
        val tracks = mutableListOf<LocalTrack>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            val duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
            val albumName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
            val albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))

            tracks.add(
                LocalTrack(
                    id = id,
                    title = title ?: "Неизвестно",
                    artist = artist ?: "Неизвестно",
                    duration = duration,
                    uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    ),
                    albumName = albumName ?: "Неизвестно",
                    albumArt = getAlbumArt(albumId)
                )
            )
        }
        return tracks
    }

    private fun getAlbumArt(albumId: Long): Uri {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }
}
