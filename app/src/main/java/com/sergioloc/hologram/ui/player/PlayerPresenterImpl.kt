package com.sergioloc.hologram.ui.player

import com.google.android.youtube.player.YouTubePlayerView

class PlayerPresenterImpl(var view: PlayerInterface.View): PlayerInterface.Presenter {

    private var interactor: PlayerInteractorImpl? = null

    override fun callPlayer(youtubePlayer: YouTubePlayerView, videoID: String) {
        interactor = PlayerInteractorImpl(this, youtubePlayer, videoID)
        interactor?.initPlayer()
    }

    override fun errorInit() {
        view.showError()
    }
}