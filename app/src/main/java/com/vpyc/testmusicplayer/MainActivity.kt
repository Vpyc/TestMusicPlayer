package com.vpyc.testmusicplayer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vpyc.testmusicplayer.app.MyApp
import com.vpyc.testmusicplayer.data.ChartRepository
import com.vpyc.testmusicplayer.ui.theme.TestMusicPlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var chartRepository: ChartRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appComponent = (applicationContext as MyApp).appComponent
        appComponent.inject(this)

        fetchChartData()
        setContent {
            TestMusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
    private fun fetchChartData() {
        CoroutineScope(Dispatchers.IO).launch {
            val chartData = chartRepository.getChartData()
            withContext(Dispatchers.Main) {
                if (chartData != null) {
                    Log.d("ChartData", chartData.toString())
                } else {
                    Log.d("ChartData", "Error fetching chart data")
                }
            }
        }
    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestMusicPlayerTheme {
        Greeting("Android")
    }
}