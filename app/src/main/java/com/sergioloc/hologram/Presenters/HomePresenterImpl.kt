package com.sergioloc.hologram.Presenters

import android.content.Context
import android.net.Uri
import android.util.Log
import com.sergioloc.hologram.Interactor.HomeInteractorImpl
import com.sergioloc.hologram.Interfaces.HomeInterface
import com.sergioloc.hologram.Models.VideoModel

class HomePresenterImpl(var view: HomeInterface.View, var context: Context): HomeInterface.Presenter {

    private var interactor: HomeInteractorImpl? = null

    init {
        interactor = HomeInteractorImpl(this, context)
    }

    override fun buttonPressed() {
        interactor?.getDemoFromFirebase()
    }

    override fun videoReady(code: String) {
        view.navigateToActivity(code)
    }

    override fun initNews() {
        interactor?.getNewsFromFirebase()
    }

    override fun sendNew(image: Uri, name: String, tag: String, position: Int) {
        view.showNew(image, name, tag, position)
    }

    override fun videoPressed(position: Int) {
        interactor?.goToYouTube(position)
    }
}