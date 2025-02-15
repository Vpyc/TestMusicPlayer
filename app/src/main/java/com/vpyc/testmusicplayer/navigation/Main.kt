package com.vpyc.testmusicplayer.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vpyc.testmusicplayer.player.PlayerView
import com.vpyc.testmusicplayer.tracklist.TrackListScreen

@Composable
fun Main(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()
    NavHost(navController = navController, NavRoutes.TrackList.route) {
        composable(NavRoutes.TrackList.route) {
            TrackListScreen(
                viewModelFactory = viewModelFactory,
                onTrackClick = { trackId, trackIds ->
                    val idsString = trackIds.joinToString(",")
                    navController.navigate(NavRoutes.Player.createRoute(idsString, trackId))
                }
            )
        }
        composable(
            route = NavRoutes.Player.route,
            arguments = listOf(
                navArgument("trackIds") { type = NavType.StringType },
                navArgument("currentTrackId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val trackIdsString = backStackEntry.arguments?.getString("trackIds") ?: ""
            val trackIds = trackIdsString.split(",").map { it.toLong() }
            val currentTrackId = backStackEntry.arguments?.getLong("currentTrackId") ?: 0L

            PlayerView(
                viewModelFactory = viewModelFactory,
                trackIds = trackIds,
                currentTrackId = currentTrackId
            )
        }
    }
}