package com.vpyc.testmusicplayer.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vpyc.testmusicplayer.service.MusicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    val playerState: StateFlow<PlayerUiState> = musicRepository.getPlayerState()
        .stateIn(viewModelScope, SharingStarted.Lazily, PlayerUiState(track = null))

    private val _intent = MutableSharedFlow<PlayerIntent>()
    private val intent: SharedFlow<PlayerIntent> = _intent

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collect { playerIntent ->
                when (playerIntent) {
                    is PlayerIntent.PlayPause -> musicRepository.togglePlayPause()
                    is PlayerIntent.Next -> musicRepository.playNextTrack()
                    is PlayerIntent.Previous -> musicRepository.playPreviousTrack()
                    is PlayerIntent.Seek -> musicRepository.seekTo(playerIntent.position)
                }
            }
        }
    }

    fun onIntent(intent: PlayerIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }
}