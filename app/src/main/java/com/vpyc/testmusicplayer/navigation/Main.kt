package com.vpyc.testmusicplayer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vpyc.testmusicplayer.player.PlayerView
import com.vpyc.testmusicplayer.tracklist.TrackListScreen

@Composable
fun Main(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoute.ONLINE_TRACKS.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoute.ONLINE_TRACKS.route) {
                TrackListScreen(
                    viewModelFactory = viewModelFactory,
                    isLocal = false,
                    onTrackClick = { trackId, trackIds, isLocal ->
                        val idsString = trackIds.joinToString(",")
                        navController.navigate(NavRoutes.Player.createRoute(
                            idsString, trackId, isLocal
                        ))
                    }
                )
            }
            composable(NavRoute.LOCAL_TRACKS.route) {
                TrackListScreen(
                    viewModelFactory = viewModelFactory,
                    isLocal = true,
                    onTrackClick = { trackId, trackIds, isLocal ->
                        val idsString = trackIds.joinToString(",")
                        navController.navigate(NavRoutes.Player.createRoute(
                            idsString, trackId, isLocal
                        ))
                    }
                )
            }
            composable(
                route = NavRoutes.Player.route,
                arguments = listOf(
                    navArgument("trackIds") { type = NavType.StringType },
                    navArgument("currentTrackId") { type = NavType.LongType },
                    navArgument("isLocal") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val trackIdsString = backStackEntry.arguments?.getString("trackIds") ?: ""
                val trackIds = trackIdsString.split(",").map { it.toLong() }
                val currentTrackId = backStackEntry.arguments?.getLong("currentTrackId") ?: 0L
                val isLocal = backStackEntry.arguments?.getBoolean("isLocal") ?: true

                PlayerView(
                    viewModelFactory = viewModelFactory,
                    trackIds = trackIds,
                    currentTrackId = currentTrackId,
                    isLocal = isLocal
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation {
        NavRoute.entries.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.route) },
                label = { Text(text = screen.route) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}