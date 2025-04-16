package com.economist.demo.pip

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.PlayerView
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.economist.demo.R

@Composable
fun PipVideoPlayer(
    videoUri: Uri,
    viewModel: VideoPlayerPipViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val player by viewModel.player.collectAsState()
    val isMuted by viewModel.isMuted.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()

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
                update = { it.setPlayer(exoPlayer) },
                modifier = Modifier.fillMaxSize()
            )

            val progress = if (duration > 0) currentPosition.toFloat() / duration else 0f

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${formatDuration(currentPosition)} / ${formatDuration(duration)}",
                        color = Color.White,
                        fontSize = 12.sp
                    )

                    Row {
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

                        IconButton(onClick = { viewModel.restartVideo() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_restart),
                                contentDescription = "Restart Video",
                                tint = Color.White
                            )
                        }
                    }
                }

                LinearProgressIndicator(
                    progress = progress.coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Red,
                    trackColor = Color.DarkGray
                )
            }
        }
    }
}


fun formatDuration(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}







