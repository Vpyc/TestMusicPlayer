package com.vpyc.testmusicplayer.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vpyc.testmusicplayer.retrofit.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BasePlayerViewModel(
    private val exoPlayer: ExoPlayer
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayerUiState(null))
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    private var trackIds: List<Long> = emptyList()
    private var currentTrackIndex: Int = 0

    private val _intent = MutableSharedFlow<PlayerIntent>()
    private val intent: SharedFlow<PlayerIntent> = _intent

    init {
        handleIntents()
        observePlayerPosition()
        addPlayerListener()
    }

    // Абстрактный метод для получения трека по id
    protected abstract suspend fun fetchTrack(trackId: Long): Track?

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collect { playerIntent ->
                when (playerIntent) {
                    is PlayerIntent.LoadTrackWithContext -> {
                        trackIds = playerIntent.trackIds
                        currentTrackIndex = trackIds.indexOf(playerIntent.currentTrackId)
                            .coerceAtLeast(0)
                        loadTrack(playerIntent.currentTrackId)
                    }
                    is PlayerIntent.PlayPause -> togglePlayPause()
                    is PlayerIntent.Next -> playNextTrack()
                    is PlayerIntent.Previous -> playPreviousTrack()
                    is PlayerIntent.Seek -> seekTo(playerIntent.position)
                }
            }
        }
    }

    protected fun loadTrack(trackId: Long) {
        viewModelScope.launch {
            val track = fetchTrack(trackId)
            _uiState.value = _uiState.value.copy(track = track)
            track?.let {
                exoPlayer.setMediaItem(
                    MediaItem.Builder()
                        .setUri(it.preview) // поле preview содержит URI (как для API, так и для локальных треков)
                        .setMediaId(trackId.toString())
                        .build()
                )
                exoPlayer.prepare()
                exoPlayer.play()
                updateUiState(isPlaying = true)
            }
        }
    }

    private fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            updateUiState(isPlaying = false)
        } else {
            exoPlayer.play()
            updateUiState(isPlaying = true)
        }
    }

    private fun playNextTrack() {
        currentTrackIndex = (currentTrackIndex + 1) % trackIds.size
        loadTrack(trackIds[currentTrackIndex])
    }

    private fun playPreviousTrack() {
        currentTrackIndex = if (currentTrackIndex == 0) trackIds.size - 1
        else currentTrackIndex - 1
        loadTrack(trackIds[currentTrackIndex])
    }

    private fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        _uiState.value = _uiState.value.copy(currentPosition = position)
    }

    private fun observePlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val duration = exoPlayer.duration.coerceAtLeast(0)
                val currentPosition = exoPlayer.currentPosition.coerceAtLeast(0)
                _uiState.value = _uiState.value.copy(
                    duration = duration,
                    currentPosition = currentPosition
                )
                delay(500L)
            }
        }
    }

    private fun addPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playNextTrack()
                }
            }
        })
    }

    protected fun updateUiState(isPlaying: Boolean) {
        _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
    }

    fun onIntent(intent: PlayerIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }
}