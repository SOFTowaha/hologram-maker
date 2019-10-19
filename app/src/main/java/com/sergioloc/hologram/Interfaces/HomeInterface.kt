package com.sergioloc.hologram.Interfaces

import android.net.Uri
import com.sergioloc.hologram.Models.VideoModel

interface HomeInterface {

    interface View {
        fun navigateToActivity(code: String)
        fun showNew(image: Uri, name: String, tag: String, position: Int)
    }

    interface Presenter {
        fun buttonPressed()
        fun videoPressed(position: Int)
        fun videoReady(code: String)
        fun initNews()
        fun sendNew(image: Uri, name: String, tag: String, position: Int)
    }

    interface Interactor {
        fun getDemoFromFirebase()
        fun getNewsFromFirebase()
        fun getUriFromStorage(video: VideoModel, position: Int)
        fun goToYouTube(position: Int)
    }

}