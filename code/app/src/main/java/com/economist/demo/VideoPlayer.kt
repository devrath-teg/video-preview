package com.economist.demo

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.unit.dp

@Composable
fun VideoPlayer(
    videoUri: Uri,
    viewModel: VideoPlayerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val player by viewModel.player.collectAsState()

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
                it.setPlayer(exoPlayer)
            },
            modifier = modifier
                .fillMaxSize()
        )
    }
}


