package com.vpyc.testmusicplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.vpyc.testmusicplayer.R
import com.vpyc.testmusicplayer.player.PlayerUiState
import com.vpyc.testmusicplayer.retrofit.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicService : Service() {

    private lateinit var exoPlayer: ExoPlayer
    private val _trackState = MutableStateFlow(PlayerUiState(null))
    val trackState: StateFlow<PlayerUiState> get() = _trackState

    private val binder = MusicBinder()

    private var trackList: List<Track> = emptyList()
    private var currentTrackIndex: Int = 0
    private var currentTrack: Track? = null

    private val scope = CoroutineScope(Dispatchers.Main)

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    playNextTrack()
                }
                updateTrackState()
            }
        })

        observeCurrentDuration()
        createNotificationChannel()

        startForeground(NOTIFICATION_ID, sendNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> {
                val current = intent.getParcelableExtra<Track>(EXTRA_CURRENT_TRACK)
                val tracks = intent.getParcelableArrayListExtra<Track>(EXTRA_TRACK_LIST)
                if (tracks != null && current != null) {
                    playTrack(tracks, current)
                }
            }

            PLAY_PAUSE -> {
                togglePlayPause()
            }

            PREV -> {
                playPreviousTrack()
            }

            NEXT -> {
                playNextTrack()
            }
        }
        return START_STICKY
    }

    private fun observeCurrentDuration() {
        scope.launch {
            while (true) {
                updateTrackState()
                delay(1000)
            }
        }
    }

    private fun playTrack(tracks: List<Track>, selectedTrack: Track) {
        trackList = tracks
        currentTrackIndex = tracks.indexOf(selectedTrack).takeIf { it >= 0 } ?: 0
        currentTrack = trackList.getOrNull(currentTrackIndex)

        val mediaItems = tracks.map { track ->
            MediaItem.Builder()
                .setUri(track.preview)
                .setMediaId(track.id.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setArtist(track.artist.name)
                        .setAlbumTitle(track.album.title)
                        .build()
                )
                .build()
        }
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
        exoPlayer.seekTo(currentTrackIndex, 0)
        exoPlayer.play()

        updateTrackState()
    }

    fun playNextTrack() {
        if (trackList.isNotEmpty()) {
            currentTrackIndex = if (exoPlayer.hasNextMediaItem()) {
                (currentTrackIndex + 1) % trackList.size
            } else {
                0
            }
            currentTrack = trackList[currentTrackIndex]
            exoPlayer.seekTo(currentTrackIndex, 0)
            exoPlayer.play()
            updateTrackState()
        }
    }

    fun playPreviousTrack() {
        if (trackList.isNotEmpty()) {
            currentTrackIndex = if (exoPlayer.hasPreviousMediaItem()) {
                if (currentTrackIndex - 1 >= 0) currentTrackIndex - 1 else trackList.size - 1
            } else {
                trackList.size - 1
            }
            currentTrack = trackList[currentTrackIndex]
            exoPlayer.seekTo(currentTrackIndex, 0)
            exoPlayer.play()
            updateTrackState()
        }
    }

    fun togglePlayPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
        updateTrackState()
    }

    fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
        updateTrackState()
    }

    private fun updateTrackState() {
        val safeDuration = if (exoPlayer.duration >= 0L) exoPlayer.duration else 0L
        _trackState.value = PlayerUiState(
            track = currentTrack,
            isPlaying = exoPlayer.isPlaying,
            currentPosition = exoPlayer.currentPosition,
            duration = safeDuration
        )
        updateNotification()
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun sendNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(currentTrack?.title)
            .setContentText(currentTrack?.artist?.name)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(R.drawable.ic_play_prev, "", createPreviousPendingIntent())
            .addAction(
                if (exoPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                "",
                createPlayPausePendingIntent()
            )
            .addAction(R.drawable.ic_play_next, "", createNextPendingIntent())
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .build()
    }

    private fun updateNotification() {
        val notification = sendNotification()
        val manager = getSystemService(NotificationManager::class.java)
        manager?.notify(NOTIFICATION_ID, notification)
    }

    private fun createPreviousPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply { action = PREV }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createPlayPausePendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply { action = PLAY_PAUSE }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun createNextPendingIntent(): PendingIntent {
        val intent = Intent(this, MusicService::class.java).apply { action = NEXT }
        return PendingIntent.getService(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    companion object {
        const val ACTION_PLAY = "ACTION_LOAD"
        const val PREV = "PREV"
        const val PLAY_PAUSE = "PLAY_PAUSE"
        const val NEXT = "NEXT"
        const val EXTRA_CURRENT_TRACK = "EXTRA_CURRENT_TRACK"
        const val EXTRA_TRACK_LIST = "EXTRA_TRACK_LIST"
        private const val CHANNEL_ID = "MusicServiceChannel"
        private const val NOTIFICATION_ID = 1
    }
}
