package com.economist.demo

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun VideoPlayer(
    videoUri: Uri,
    viewModel: VideoPlayerViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val player by viewModel.player.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    LaunchedEffect(videoUri) {
        viewModel.playNewVideo(context, videoUri)
    }

    player?.let { exoPlayer ->
        Box(modifier = modifier) {
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
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            ) {
                IconButton(onClick = { viewModel.toggleMute() }) {
                    Icon(
                        painter = painterResource(
                            id = if (isMuted)
                                R.drawable.ic_volume_off
                            else
                                R.drawable.ic_volume_on
                        ),
                        contentDescription = "Toggle Mute",
                        tint = Color.White
                    )
                }

                IconButton(onClick = { viewModel.togglePlayPause() }) {
                    Icon(
                        painter = painterResource(
                            id = if (isPlaying)
                                R.drawable.ic_pause_circle
                            else
                                R.drawable.ic_play_circle
                        ),
                        contentDescription = "Toggle Play",
                        tint = Color.White
                    )
                }
            }
        }
    }
}



