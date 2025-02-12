package com.vpyc.testmusicplayer.data

import com.vpyc.testmusicplayer.retrofit.ChartApi
import com.vpyc.testmusicplayer.retrofit.Track
import javax.inject.Inject

class ChartRepositoryImpl @Inject constructor(
    private val chartApi: ChartApi): ChartRepository {
    override suspend fun getChartData(): List<Track>? {
        val response = chartApi.getChart()
        return if (response.isSuccessful) {
            response.body()?.tracks?.data
        } else {
            null
        }
    }
}

interface ChartRepository {
    suspend fun getChartData(): List<Track>?
}
