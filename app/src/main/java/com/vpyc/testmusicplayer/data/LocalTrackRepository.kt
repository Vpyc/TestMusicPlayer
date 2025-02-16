package com.vpyc.testmusicplayer.data

import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalTracksRepository @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper
) {
    suspend fun getLocalTracks(): List<LocalTrack> {
        return withContext(Dispatchers.IO) {
            contentResolverHelper.queryTracks(
                selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0",
                selectionArgs = null,
                sortOrder = null
            )
        }
    }

    suspend fun searchLocalTracks(query: String): List<LocalTrack> {
        return withContext(Dispatchers.IO) {
            val searchPattern = "%$query%"
            contentResolverHelper.queryTracks(
                selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 AND " +
                        "(${MediaStore.Audio.Media.TITLE} LIKE ? OR " +
                        "${MediaStore.Audio.Media.ARTIST} LIKE ? OR " +
                        "${MediaStore.Audio.Media.ALBUM} LIKE ?)",
                selectionArgs = arrayOf(searchPattern, searchPattern, searchPattern),
                sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
            )
        }
    }

    suspend fun getTrackById(id: Long): LocalTrack? {
        return withContext(Dispatchers.IO) {
            contentResolverHelper.queryTracks(
                selection = "${MediaStore.Audio.Media._ID} = ?",
                selectionArgs = arrayOf(id.toString()),
                sortOrder = null
            ).firstOrNull()
        }
    }
}