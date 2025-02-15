package com.vpyc.testmusicplayer.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavRoutes(val route: String) {
    object Player : NavRoutes(
        "player?trackIds={trackIds}&currentTrackId={currentTrackId}&isLocal={isLocal}"
    ) {
        fun createRoute(trackIds: String, currentTrackId: Long, isLocal: Boolean) =
            "player?trackIds=$trackIds&currentTrackId=$currentTrackId&isLocal=$isLocal"
    }
}

enum class NavRoute(val route: String, val icon: ImageVector) {
    LOCAL_TRACKS("local_tracks", Icons.Default.Menu),
    ONLINE_TRACKS("online_tracks", Icons.Default.CheckCircle)
}