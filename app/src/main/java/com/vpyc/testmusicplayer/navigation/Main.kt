package com.vpyc.testmusicplayer.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                    onTrackClick = { currentTrack  ->
                        navController.navigate(NavRoutes.Player.createRoute(
                            currentTrack
                        ))
                    }
                )
            }
            composable(NavRoute.LOCAL_TRACKS.route) {
                TrackListScreen(
                    viewModelFactory = viewModelFactory,
                    isLocal = true,
                    onTrackClick = { currentTrack  ->
                        navController.navigate(NavRoutes.Player.createRoute(
                            currentTrack
                        ))
                    }
                )
            }
            composable(route = NavRoutes.Player.route) {
                PlayerView(
                    viewModelFactory = viewModelFactory,
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = colorScheme.background,
        contentColor = colorScheme.onPrimaryContainer,
    ) {
        NavRoute.entries.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = screen.title
                    )
                },
                label = { Text(text = screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

