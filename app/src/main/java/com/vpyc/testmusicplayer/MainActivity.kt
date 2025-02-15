package com.vpyc.testmusicplayer

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.vpyc.testmusicplayer.app.MyApp
import com.vpyc.testmusicplayer.navigation.Main
import com.vpyc.testmusicplayer.ui.theme.TestMusicPlayerTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    private val REQUEST_CODE_PERMISSIONS = 1001

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        } else {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSIONS
                )
            }
        }

        val appComponent = (applicationContext as MyApp).appComponent
        appComponent.inject(this)

        setContent {
            TestMusicPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Main(viewModelFactory)
                }
            }
        }
    }
}