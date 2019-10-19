package com.sergioloc.hologram.Views

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.sergioloc.hologram.Interfaces.PlayerInterface
import com.sergioloc.hologram.Presenters.PlayerPresenterImpl
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity: YouTubeBaseActivity(), PlayerInterface.View {

    private var presenter: PlayerPresenterImpl? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        var player = youtube_player
        var videoID = intent.extras!!.getString("id")
        presenter = PlayerPresenterImpl(this)
        presenter?.callPlayer(player, videoID)
    }

    override fun showError() {
        Toast.makeText(this, R.string.errorYT, Toast.LENGTH_LONG).show()
    }
}