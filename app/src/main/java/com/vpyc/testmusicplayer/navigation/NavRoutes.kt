package com.vpyc.testmusicplayer.navigation

sealed class NavRoutes(val route: String) {
    data object TrackList : NavRoutes("track_list")
    object Player : NavRoutes("player?trackIds={trackIds}&currentTrackId={currentTrackId}") {
        fun createRoute(trackIds: String, currentTrackId: Long) =
            "player?trackIds=$trackIds&currentTrackId=$currentTrackId"
    }
}