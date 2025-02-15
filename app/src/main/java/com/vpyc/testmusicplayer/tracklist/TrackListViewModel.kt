package com.vpyc.testmusicplayer.tracklist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpyc.testmusicplayer.data.ChartRepository
import com.vpyc.testmusicplayer.retrofit.Track
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrackListViewModel @Inject constructor(
    private val chartRepository: ChartRepository
) : ViewModel() {

    private val _state = mutableStateOf<TrackListState>(TrackListState.Loading)
    val state: State<TrackListState> get() = _state

    fun handleIntent(intent: TrackListIntent) {
        when (intent) {
            is TrackListIntent.LoadTracks -> { fetchChartData() }
            is TrackListIntent.SearchTracks -> handleSearch(intent.query)
        }
    }

    private fun fetchChartData() {
        viewModelScope.launch {
            _state.value = TrackListState.Loading
            try {
                val tracks = chartRepository.getChartData()
                updateStateWithTracks(tracks, "")
            } catch (e: Exception) {
                _state.value = TrackListState.Error("Failed to load data: ${e.message}")
            }
        }
    }

    private fun handleSearch(query: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is TrackListState.Success) {
                _state.value = currentState.copy(searchQuery = query)
            }

            try {
                val tracks = if (query.isBlank()) {
                    chartRepository.getChartData()
                } else {
                    chartRepository.searchTracks(query)
                }
                updateStateWithTracks(tracks, query)
            } catch (e: Exception) {
                _state.value = TrackListState.Error("Search failed: ${e.message}")
            }
        }
    }

    private fun updateStateWithTracks(tracks: List<Track>?, query: String) {
        _state.value = if (tracks.isNullOrEmpty()) {
            TrackListState.Error("No tracks found")
        } else {
            TrackListState.Success(
                tracks = tracks,
                trackIds = tracks.map { it.id },
                searchQuery = query
            )
        }
    }
}