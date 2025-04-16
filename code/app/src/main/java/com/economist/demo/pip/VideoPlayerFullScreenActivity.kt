package com.economist.demo.pip

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import com.economist.demo.R

// VideoPlayerFullScreenActivity.kt
@androidx.annotation.OptIn(UnstableApi::class)
class VideoPlayerFullScreenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { SetContentForScreen() }
    }

    @Composable
    private fun SetContentForScreen() {
        val viewModel: VideoPlayerPipViewModel = viewModel()
        val uri = Uri.parse(viewModel.videoUrl)
        val isInPipMode = remember { mutableStateOf(false) }
        val lifecycleOwner = LocalLifecycleOwner.current
        val configuration = LocalConfiguration.current

        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    isInPipMode.value = this@VideoPlayerFullScreenActivity.isInPictureInPictureMode
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        if (isLandscape) {
            // Fullscreen video in landscape
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                PipVideoPlayer(videoUri = uri, viewModel = viewModel)
            }
        } else {
            // Portrait mode: video + controls
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PipVideoPlayer(videoUri = uri, viewModel = viewModel)

                if (!isInPipMode.value) {
                    IconButton(onClick = { enterPipMode() }) {
                        Icon(
                            painterResource(id = R.drawable.ic_picture_in_picture),
                            contentDescription = "Enter PiP",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Video Title",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )

                    // Add more UI here if needed
                }
            }
        }
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(16, 9)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPipMode()
    }
}


