package com.sergioloc.hologram.Interfaces

import com.google.android.youtube.player.YouTubePlayerView

interface PlayerPresenter {
    fun callPlayer(youtubePlayer: YouTubePlayerView, videoID: String)
    fun errorInit()
}