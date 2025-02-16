package com.vpyc.testmusicplayer.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.vpyc.testmusicplayer.player.PlayerUiState
import com.vpyc.testmusicplayer.retrofit.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MusicRepository @Inject constructor(
    private val context: Context
) {
    private var musicService: MusicService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            isBound = false
        }
    }

    fun release() {
        if (isBound) {
            context.unbindService(connection)
            isBound = false
            musicService = null
        }
    }

    fun loadTracks(currentTrack: Track, tracks: List<Track>) {
        val intent = Intent(context, MusicService::class.java).apply {
            action = MusicService.ACTION_PLAY
            putExtra(MusicService.EXTRA_CURRENT_TRACK, currentTrack)
            putParcelableArrayListExtra(MusicService.EXTRA_TRACK_LIST, ArrayList(tracks))
        }
        context.startService(intent)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun getPlayerState(): StateFlow<PlayerUiState> {
        return musicService?.trackState ?: MutableStateFlow(PlayerUiState(null)).asStateFlow()
    }

    fun togglePlayPause() {
        musicService?.togglePlayPause()
    }

    fun playNextTrack() {
        musicService?.playNextTrack()
    }

    fun playPreviousTrack() {
        musicService?.playPreviousTrack()
    }

    fun seekTo(position: Long) {
        musicService?.seekTo(position)
    }

}