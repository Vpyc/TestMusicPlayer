package com.vpyc.testmusicplayer.tracklist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpyc.testmusicplayer.data.ChartRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackListViewModel @Inject constructor(
    private val chartRepository: ChartRepository
) : ViewModel() {

    private val _state = mutableStateOf<TrackListState>(TrackListState.Loading)
    val state: State<TrackListState> get() = _state

    fun handleIntent(intent: TrackListIntent) {
        when (intent) {
            is TrackListIntent.LoadTracks -> {
                fetchChartData()
            }
        }
    }

    private fun fetchChartData() {
        viewModelScope.launch {
            _state.value = TrackListState.Loading
            try {
                val tracks = chartRepository.getChartData()
                _state.value = if (tracks.isNullOrEmpty()) {
                    TrackListState.Error("No tracks available")
                } else {
                    val trackIds = tracks.map { it.id }
                    TrackListState.Success(tracks, trackIds)
                }
            } catch (e: Exception) {
                _state.value = TrackListState.Error("Failed to load data: ${e.message}")
            }
        }
    }
}