package com.sergioloc.hologram.Presenters

import com.google.android.youtube.player.YouTubePlayerView
import com.sergioloc.hologram.Interactor.PlayerInteractorImpl
import com.sergioloc.hologram.Interfaces.PlayerInterface

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