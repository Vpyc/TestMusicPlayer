package com.vpyc.testmusicplayer.navigation

import com.vpyc.testmusicplayer.R

sealed class NavRoutes(val route: String) {
    object Player : NavRoutes(
        "player?track={track}"
    ) {
        fun createRoute(track: Long): String {
            return "player?track=$track"
        }
    }
}

enum class NavRoute(val route: String, val icon: Int, val title: String) {
    LOCAL_TRACKS("local_tracks", R.drawable.ic_local, "Локальные треки"),
    ONLINE_TRACKS("online_tracks", R.drawable.ic_api, "Треки из API")
}