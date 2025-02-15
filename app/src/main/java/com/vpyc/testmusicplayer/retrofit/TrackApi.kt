package com.vpyc.testmusicplayer.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TrackApi {
    @GET("track/{id}")
    suspend fun getTrackByUrl(@Path("id") trackId: Long): Response<Track>
}