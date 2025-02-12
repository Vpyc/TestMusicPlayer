package com.vpyc.testmusicplayer.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface ChartApi {
    @GET("chart")
    suspend fun getChart(): Response<ChartResponse>
}