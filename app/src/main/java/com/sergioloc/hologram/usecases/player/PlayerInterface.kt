package com.sergioloc.hologram.usecases.player

import com.google.android.youtube.player.YouTubePlayerView

interface PlayerInterface {

    interface View {
        fun showError()
    }

    interface Presenter {
        fun callPlayer(youtubePlayer: YouTubePlayerView, videoID: String)
        fun errorInit()
    }

    interface Interactor {
        fun initPlayer()
    }
}