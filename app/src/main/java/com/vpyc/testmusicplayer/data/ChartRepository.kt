package com.vpyc.testmusicplayer.data

import com.vpyc.testmusicplayer.retrofit.ChartApi
import com.vpyc.testmusicplayer.retrofit.SearchApi
import com.vpyc.testmusicplayer.retrofit.Track
import javax.inject.Inject

class ChartRepositoryImpl @Inject constructor(
    private val chartApi: ChartApi,
    private val searchApi: SearchApi,
): ChartRepository {
    override suspend fun getChartData(): List<Track>? {
        val response = chartApi.getChart()
        return if (response.isSuccessful) {
            response.body()?.tracks?.data
        } else {
            null
        }
    }

    override suspend fun searchTracks(query: String): List<Track>? {
        val response = searchApi.searchTracks(query)
        return if (response.isSuccessful) {
            response.body()?.data
        } else {
            null
        }
    }
}

interface ChartRepository {
    suspend fun getChartData(): List<Track>?

    suspend fun searchTracks(query: String): List<Track>?
}
