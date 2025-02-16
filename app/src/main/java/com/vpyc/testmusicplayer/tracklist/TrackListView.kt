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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.vpyc.testmusicplayer.R
import com.vpyc.testmusicplayer.customView.IsLoading
import com.vpyc.testmusicplayer.retrofit.Track

@Composable
fun TrackListScreen(
    viewModelFactory: ViewModelProvider.Factory,
    isLocal: Boolean,
    onTrackClick: (Long) -> Unit
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
                LazyColumn {
                    items(tracks) { track ->
                        TrackCard(
                            track = track,
                            allTracks = tracks,
                            onTrackClick = onTrackClick,
                            viewModel
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
fun TrackCard(track: Track,
              allTracks: List<Track>,
              onTrackClick: (Long) -> Unit,
              viewModel: BaseTrackListViewModel)
{
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                viewModel.onTrackClick(track, allTracks)
                onTrackClick( track.id )
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = track.album.cover,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                placeholder = painterResource(id = R.drawable.ic_album),
                error = painterResource(id = R.drawable.ic_album)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = track.title, style = MaterialTheme.typography.bodyLarge)
                Text(text = track.artist.name, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}