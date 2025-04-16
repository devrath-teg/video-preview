package com.economist.demo.pip

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VideoPlayerNavHost(
    onEnterPip: () -> Unit,
    onEnterFullscreen: () -> Unit,
    onExitFullscreen: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "player"
    ) {
        composable("launch") {
            LaunchScreen(onStartClicked = {
                navController.navigate("player")
            })
        }

        composable("player") {
            VideoPlayerScreen(
                onEnterPip = onEnterPip,
                onEnterFullscreen = onEnterFullscreen,
                onExitFullscreen = onExitFullscreen
            )
        }
    }
}
