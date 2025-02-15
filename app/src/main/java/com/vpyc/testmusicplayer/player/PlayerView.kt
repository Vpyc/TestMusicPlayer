package com.vpyc.testmusicplayer.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun PlayerView(
    viewModelFactory: ViewModelProvider.Factory,
    trackIds: List<Long>,
    currentTrackId: Long,
    isLocal: Boolean,
) {
    val viewModel: BasePlayerViewModel = if (isLocal)
        viewModel<LocalPlayerViewModel>(factory = viewModelFactory)
    else viewModel<OnlinePlayerViewModel>(factory = viewModelFactory)

    LaunchedEffect(trackIds, currentTrackId) {
        viewModel.onIntent(
            PlayerIntent.LoadTrackWithContext(
                trackIds = trackIds,
                currentTrackId = currentTrackId
            )
        )
    }

    val state by viewModel.uiState.collectAsState()

    state.track?.let { track ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = track.album.cover,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Text(text = track.title, style = MaterialTheme.typography.labelLarge)
            track.album.title?.let {
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
            Text(text = track.artist.name, style = MaterialTheme.typography.labelLarge)

            Slider(
                value = state.currentPosition.toFloat(),
                onValueChange = {
                    viewModel.onIntent(PlayerIntent.Seek(it.toLong()))
                },
                onValueChangeFinished = {
                    viewModel.onIntent(PlayerIntent.Seek(state.currentPosition))
                },
                valueRange = 0f..state.duration.toFloat()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${state.currentPosition / 1000}s")
                Text(text = "${state.duration / 1000}s")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { viewModel.onIntent(PlayerIntent.Previous) }) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Предыдущий")
                }
                IconButton(onClick = { viewModel.onIntent(PlayerIntent.PlayPause) }) {
                    Icon(
                        if (state.isPlaying) Icons.Default.Person else Icons.Default.PlayArrow,
                        contentDescription = "Воспроизведение/Пауза"
                    )
                }
                IconButton(onClick = { viewModel.onIntent(PlayerIntent.Next) }) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Следующий")
                }
            }
        }
    }
}