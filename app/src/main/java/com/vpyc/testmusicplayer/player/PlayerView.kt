package com.vpyc.testmusicplayer.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.vpyc.testmusicplayer.R

@Composable
fun PlayerView(
    viewModelFactory: ViewModelProvider.Factory,
) {
    val viewModel = viewModel<PlayerViewModel>(factory = viewModelFactory)

    val state by viewModel.playerState.collectAsState()

    state.track?.let { track ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = track.album.cover,
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.ic_album),
                error = painterResource(id = R.drawable.ic_album),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(300.dp)
            )

            Text(
                text = track.title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1
            )

            track.album.title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(
                text = track.artist.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
            )

            Slider(
                value = state.currentPosition.toFloat(),
                onValueChange = {
                    viewModel.onIntent(PlayerIntent.Seek(it.toLong()))
                },
                onValueChangeFinished = {
                    viewModel.onIntent(PlayerIntent.Seek(state.currentPosition))
                },
                valueRange = 0f..state.duration.toFloat(),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(state.currentPosition),
                    style = MaterialTheme.typography.labelSmall
                )
                Text(
                    text = formatTime(state.duration),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.onIntent(PlayerIntent.Previous) },
                    modifier = Modifier
                        .size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play_prev),
                        contentDescription = "Предыдущий",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = { viewModel.onIntent(PlayerIntent.PlayPause) },
                    modifier = Modifier
                        .size(80.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (state.isPlaying) R.drawable.ic_pause
                            else R.drawable.ic_play
                        ),
                        contentDescription = "Воспроизведение/Пауза",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = { viewModel.onIntent(PlayerIntent.Next) },
                    modifier = Modifier
                        .size(64.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play_next),
                        contentDescription = "Следующий",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
