package com.vpyc.testmusicplayer.tracklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.vpyc.testmusicplayer.customView.IsLoading

@Composable
fun TrackListScreen(
    viewModelFactory: ViewModelProvider.Factory,
    isLocal: Boolean,
    onTrackClick: (Long, List<Long>) -> Unit
) {
    val viewModel: BaseTrackListViewModel = if (isLocal) {
        viewModel<LocalTrackListViewModel>(factory = viewModelFactory)
    } else {
        viewModel<OnlineTrackListViewModel>(factory = viewModelFactory)
    }

    val state = viewModel.state.value
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.handleIntent(TrackListIntent.LoadTracks)
    }

    Column {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.handleIntent(TrackListIntent.SearchTracks(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Найдите трек, исполнителя...") }
        )

        when (state) {
            is TrackListState.Loading -> {
                IsLoading()
            }
            is TrackListState.Success -> {
                val tracks = state.tracks
                val allIds = state.trackIds
                LazyColumn {
                    items(tracks) { track ->
                        TrackCard(
                            trackId = track.id,
                            allIds = allIds,
                            trackName = track.title,
                            artistName = track.artist.name,
                            imageUrl = track.album.cover,
                            onTrackClick = onTrackClick
                        )
                    }
                }
            }
            is TrackListState.Error -> {
                Text(text = state.message)
            }
        }
    }
}

@Composable
fun TrackCard(trackId: Long,
              allIds: List<Long>,
              trackName: String,
              artistName: String,
              imageUrl: String,
              onTrackClick: (Long, List<Long>) -> Unit)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onTrackClick(trackId, allIds) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = trackName, style = MaterialTheme.typography.bodyLarge)
                Text(text = artistName, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}