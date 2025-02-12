package com.vpyc.testmusicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.vpyc.testmusicplayer.app.MyApp
import com.vpyc.testmusicplayer.tracklist.TrackListIntent
import com.vpyc.testmusicplayer.tracklist.TrackListState
import com.vpyc.testmusicplayer.tracklist.TrackListViewModel
import com.vpyc.testmusicplayer.ui.theme.TestMusicPlayerTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val trackListViewModel: TrackListViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (applicationContext as MyApp).appComponent
        appComponent.inject(this)

        setContent {
            TestMusicPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TrackListScreen(viewModel = trackListViewModel)
                }
            }
        }
    }
}

@Composable
fun TrackListScreen(viewModel: TrackListViewModel) {
    val state = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.handleIntent(TrackListIntent.LoadTracks)
    }

    when (state) {
        is TrackListState.Loading -> {
            Text(text = "Loading...")
        }
        is TrackListState.Success -> {
            val tracks = state.tracks
            LazyColumn {
                items(tracks) { track ->
                    TrackCard(
                        trackName = track.title,
                        artistName = track.artist.name,
                        imageUrl = track.album.cover
                    )
                }
            }
        }
        is TrackListState.Error -> {
            Text(text = state.message)
        }
    }
}

@Composable
fun TrackCard(trackName: String, artistName: String, imageUrl: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(64.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = trackName, style = MaterialTheme.typography.titleMedium)
            Text(text = artistName, style = MaterialTheme.typography.bodySmall)
        }
    }
}