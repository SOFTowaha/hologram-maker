package com.sergioloc.hologram.Presenters

import com.google.firebase.auth.FirebaseAuth
import com.sergioloc.hologram.Interactor.CatalogInteractorImpl
import com.sergioloc.hologram.Interactor.NavigateInteractorImpl
import com.sergioloc.hologram.Interfaces.NavigateInterface

class NavigatePresenterImpl(var view: NavigateInterface.View): NavigateInterface.Presenter {

    private var interactor: NavigateInteractorImpl? = null

    init {
        interactor = NavigateInteractorImpl(this)
    }

    override fun checkUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            view.showAsUser(user.email.toString())
        else
            view.showAsGuest()
    }

    override fun getShareLink() {
        return interactor!!.shareLink()
    }

    override fun sendShareLink(link: String) {
        view.updateShareLink(link)
    }

}