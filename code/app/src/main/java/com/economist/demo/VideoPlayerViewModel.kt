package com.economist.demo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoPlayerViewModel : ViewModel() {
    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player: StateFlow<ExoPlayer?> = _player

    val videoUris = listOf(
        Uri.parse("https://bestvpn.org/html5demos/assets/dizzy.mp4"),
        Uri.parse("https://file-examples.com/storage/fe2465184067ef97996fb41/2017/04/file_example_MP4_480_1_5MG.mp4"),
        Uri.parse("https://files.testfile.org/Video%20MP4%2FRoad%20-%20testfile.org.mp4"),
        Uri.parse("https://files.testfile.org/Video%20MP4%2FSand%20-%20testfile.org.mp4"),
        Uri.parse("https://files.testfile.org/Video%20MP4%2FRiver%20-%20testfile.org.mp4"),
        Uri.parse("https://files.testfile.org/Video%20MP4%2FRecord%20-%20testfile.org.mp4"),
        Uri.parse("https://files.testfile.org/Video%20MP4%2FPeople%20-%20testfile.org.mp4")
    )

    fun playNewVideo(context: Context, videoUri: Uri) {
        // Whenever we are playing a new video, we need to release the old player
        releasePlayer()
        // Reset the player instance to initial points
        val exoPlayer = setUpExoPlayer(context, videoUri)
        // Set the new player instance
        _player.value = exoPlayer
    }

    private fun setUpExoPlayer(context: Context, videoUri: Uri) = ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(videoUri))
        prepare()
        seekTo(0)
        playWhenReady = true
    }

    private fun releasePlayer() {
        _player.value?.release()
        _player.value = null
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
