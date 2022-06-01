package com.sergioloc.hologram.ui.player

import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.sergioloc.hologram.R
import com.sergioloc.hologram.databinding.ActivityPlayerBinding
import com.sergioloc.hologram.utils.Constants

class PlayerActivity: YouTubeBaseActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private var videoId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVariables()
        initView()
    }

    private fun initVariables() {
        intent.extras?.getString("videoId")?.let {
            videoId = it
        }
    }

    private fun initView() {
        binding.ytPlayer.initialize(
            Constants.YOUTUBE_API_KEY,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(provider: YouTubePlayer.Provider, youTubePlayer: YouTubePlayer, b: Boolean) {
                    youTubePlayer.loadVideo(videoId)
                    youTubePlayer.setFullscreen(true)
                    youTubePlayer.play()
                }

                override fun onInitializationFailure(provider: YouTubePlayer.Provider, youTubeInitializationResult: YouTubeInitializationResult) {
                    Toast.makeText(applicationContext, getString(R.string.error_youtube), Toast.LENGTH_SHORT).show()
                }
            })
    }

}