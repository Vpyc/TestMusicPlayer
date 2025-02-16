package com.vpyc.testmusicplayer.tracklist

import com.vpyc.testmusicplayer.data.ChartRepository
import com.vpyc.testmusicplayer.retrofit.Track
import com.vpyc.testmusicplayer.service.MusicRepository
import javax.inject.Inject

class OnlineTrackListViewModel @Inject constructor(
    private val chartRepository: ChartRepository,
    musicRepository: MusicRepository
) : BaseTrackListViewModel(musicRepository) {

    override suspend fun getTracks(query: String): List<Track>? {
        return if (query.isBlank()) {
            chartRepository.getChartData()
        } else {
            chartRepository.searchTracks(query)
        }
    }
}