package com.sergioloc.hologram.Views

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.sergioloc.hologram.Interfaces.ImageInterface
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.activity_show_loaded.*

class ImageActivity: AppCompatActivity(), ImageInterface.View {

    private var imageUri: Uri? = null
    private var toolbarVisible: Boolean? = null
    private var mHideHandler: Handler? = null
    private val UI_ANIMATION_DELAY = 300

    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar
        fullscreen_content.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        val actionBar = supportActionBar
        actionBar?.show()
    }

    private val mHideRunnable = Runnable { hideToolbar() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_show_loaded)

        imageUri = Uri.parse(intent.extras!!.getString("imageUri"))
        toolbarVisible = false
        mHideHandler = Handler()

        Glide.with(this)
                .load(imageUri)
                .into(iv_picture1)

        Glide.with(this)
                .load(imageUri)
                .into(iv_picture2)

        Glide.with(this)
                .load(imageUri)
                .into(iv_picture3)

        Glide.with(this)
                .load(imageUri)
                .into(iv_picture4)

        fullscreen_content.setOnClickListener {
            if (toolbarVisible!!) {
                hideToolbar()
            }
            else {
                showToolbar()
            }
        }
    }

    override fun showToolbar() {
        // Show the system bar
        fullscreen_content.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        toolbarVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler?.removeCallbacks(mHidePart2Runnable)
        mHideHandler?.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    override fun hideToolbar() {
        val actionBar = supportActionBar
        actionBar?.hide()
        toolbarVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler?.removeCallbacks(mShowPart2Runnable)
        mHideHandler?.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun delayedHide(delayMillis: Int) {
        mHideHandler?.removeCallbacks(mHideRunnable)
        mHideHandler?.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delayedHide(100)
    }

}