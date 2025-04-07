package com.economist.demo

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ClippingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
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


    private val _isMuted = MutableStateFlow(true)
    val isMuted: StateFlow<Boolean> = _isMuted

    private val _isPlaying = MutableStateFlow(true)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val previewDurationMillis = 5_000L // configurable preview duration, e.g., 5 seconds

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

        exoPlayer.volume = if (_isMuted.value) 0f else 1f
        exoPlayer.playWhenReady = _isPlaying.value

        _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 1L

        progressJob = viewModelScope.launch {
            while (true) {
                _currentPosition.value = exoPlayer.currentPosition
                _duration.value = exoPlayer.duration.takeIf { it > 0 } ?: 1L
                delay(500L)
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun setUpExoPlayer(context: Context, videoUri: Uri): ExoPlayer {
        val mediaItem = MediaItem.fromUri(videoUri)

        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        val clippingSource = ClippingMediaSource(
            mediaSource,
            0L,
            previewDurationMillis * 1_000
        )

        return ExoPlayer.Builder(context).build().apply {
            setMediaSource(clippingSource)
            prepare()
            seekTo(0)
            playWhenReady = true
        }
    }


    private fun releasePlayer() {
        progressJob?.cancel()
        progressJob = null
        _player.value?.release()
        _player.value = null
        _currentPosition.value = 0L
        _duration.value = 1L
    }


    fun toggleMute() {
        _isMuted.value = !_isMuted.value
        _player.value?.volume = if (_isMuted.value) 0f else 1f
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
        _player.value?.playWhenReady = _isPlaying.value
    }


    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }
}
