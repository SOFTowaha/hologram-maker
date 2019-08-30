package com.sergioloc.hologram.Presenters

import com.sergioloc.hologram.Interactor.HomeInteractorImpl
import com.sergioloc.hologram.Interfaces.HomeInterface

class HomePresenterImpl(var view: HomeInterface.View): HomeInterface.Presenter {

    private var interactor: HomeInteractorImpl? = null

    override fun buttonPressed() {
        interactor = HomeInteractorImpl(this)
        interactor?.getDemoFromFirebase()
    }

    override fun videoReady(code: String) {
        view.navigateToActivity(code)
    }
}