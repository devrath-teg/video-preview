package com.economist.demo

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoPlayerViewModel : ViewModel() {
    private val _player = MutableStateFlow<ExoPlayer?>(null)
    val player: StateFlow<ExoPlayer?> = _player

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition

    private val _duration = MutableStateFlow(1L) // Avoid division by 0
    val duration: StateFlow<Long> = _duration

    private var progressJob: Job? = null

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
        releasePlayer()

        val exoPlayer = setUpExoPlayer(context, videoUri)
        _player.value = exoPlayer

        _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 1L

        progressJob = viewModelScope.launch {
            while (true) {
                _currentPosition.value = exoPlayer.currentPosition
                _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 1L
                delay(500L)
            }
        }
    }

    private fun setUpExoPlayer(context: Context, videoUri: Uri) = ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(videoUri))
        prepare()
        seekTo(0)
        playWhenReady = true
    }

    private fun releasePlayer() {
        progressJob?.cancel()
        progressJob = null
        _player.value?.release()
        _player.value = null
        _currentPosition.value = 0L
        _duration.value = 1L
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
