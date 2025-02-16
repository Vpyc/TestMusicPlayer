package com.vpyc.testmusicplayer.data

import com.vpyc.testmusicplayer.retrofit.Track
import com.vpyc.testmusicplayer.retrofit.TrackApi
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackApi: TrackApi
): TrackRepository {
    override suspend fun getTrackById(id: Long): Track? {
        val response = trackApi.getTrackByUrl(id)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}

interface TrackRepository {
    suspend fun getTrackById(id: Long): Track?
}