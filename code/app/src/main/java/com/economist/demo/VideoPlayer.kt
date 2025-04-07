package com.economist.demo

import android.net.Uri
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(
    videoUri: Uri,
    viewModel: VideoPlayerViewModel
) {
    val context = LocalContext.current
    val player by viewModel.player.collectAsState()

    // <---- Trigger reinitialization every time a new videoUri is passed ---->
    LaunchedEffect(videoUri) {
        viewModel.playNewVideo(context, videoUri)
    }

    player?.let { exoPlayer ->
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    setPlayer(exoPlayer)
                    useController = false
                }
            },
            update = {
                it.setPlayer(exoPlayer) // This avoids val reassignment issue
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
    }
}

