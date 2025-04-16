package com.economist.demo.pip

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class VideoPlayerFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {

            // Cast the hosting activity to your specific activity class
            val activity = requireActivity() as VideoPlayerFullScreenActivity

            VideoPlayerNavHost(
                onEnterPip = {
                    activity.enterPipMode()
                },
                onEnterFullscreen = {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    activity.setOrientation()
                },
                onExitFullscreen = {
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                    activity.setOrientation()
                }
            )
        }
    }
}
