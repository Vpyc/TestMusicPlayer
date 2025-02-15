package com.vpyc.testmusicplayer.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("search")
    suspend fun searchTracks(@Query("q") query: String): Response<Tracks>
}