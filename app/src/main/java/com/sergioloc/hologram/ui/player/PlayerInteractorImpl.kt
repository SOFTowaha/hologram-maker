package com.sergioloc.hologram.ui.player

import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class PlayerInteractorImpl(var presenter: PlayerInterface.Presenter, var youtubePlayer: YouTubePlayerView, var videoID: String):
        PlayerInterface.Interactor, YouTubePlayer.OnInitializedListener {

    val API_KEY = "AIzaSyAqvVJKQOfLavz-wKTT-vkkG7e1GykFn7c"

    override fun initPlayer() {
        youtubePlayer.initialize(API_KEY, this)
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, result: YouTubeInitializationResult?) {
        presenter.errorInit()
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        /** add listeners to YouTubePlayer instance **/
        player!!.setPlayerStateChangeListener(playerStateChangeListener)
        player!!.setPlaybackEventListener(playbackEventListener)

        /** Start buffering **/
        if (!wasRestored) {
            player.cueVideo(videoID)
        }
    }

    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        override fun onBuffering(arg0: Boolean) {}
        override fun onPaused() {}
        override fun onPlaying() {}
        override fun onSeekTo(arg0: Int) {}
        override fun onStopped() {}
    }

    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onAdStarted() {}
        override fun onError(arg0: YouTubePlayer.ErrorReason) {}
        override fun onLoaded(arg0: String) {}
        override fun onLoading() {}
        override fun onVideoEnded() {}
        override fun onVideoStarted() {}
    }
}