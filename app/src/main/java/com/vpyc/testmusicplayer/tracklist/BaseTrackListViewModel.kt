package com.vpyc.testmusicplayer.tracklist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpyc.testmusicplayer.retrofit.Track
import com.vpyc.testmusicplayer.service.MusicRepository
import kotlinx.coroutines.launch

abstract class BaseTrackListViewModel(
    private val musicRepository: MusicRepository
): ViewModel() {
    protected val _state = mutableStateOf<TrackListState>(TrackListState.Loading)
    val state get() = _state

    fun handleIntent(intent: TrackListIntent) {
        when (intent) {
            is TrackListIntent.LoadTracks -> fetchTracksData("")
            is TrackListIntent.SearchTracks -> fetchTracksData(intent.query)
        }
    }

    /**
     * Абстрактный метод для получения списка треков.
     * Если query пустой — загрузка всех треков, иначе — поиск.
     */
    protected abstract suspend fun getTracks(query: String): List<Track>?

    private fun fetchTracksData(query: String) {
        viewModelScope.launch {
            _state.value = TrackListState.Loading
            try {
                val tracks = getTracks(query)
                updateStateWithTracks(tracks, query)
            } catch (e: Exception) {
                _state.value = TrackListState.Error("Failed to load data: ${e.message}")
            }
        }
    }

    protected fun updateStateWithTracks(tracks: List<Track>?, query: String) {
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

    fun onTrackClick(currentTrack: Track, tracks: List<Track>) {
        musicRepository.loadTracks(currentTrack, tracks)
    }

    override fun onCleared() {
        super.onCleared()
        musicRepository.release()
    }
}