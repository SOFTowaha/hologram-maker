package com.sergioloc.hologram.usecases.navigator

import com.google.firebase.auth.FirebaseAuth

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