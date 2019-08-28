package com.sergioloc.hologram.Presenters

import com.google.firebase.auth.FirebaseAuth
import com.sergioloc.hologram.Interfaces.NavigateInterface

class NavigatePresenterImpl(var view: NavigateInterface.View): NavigateInterface.Presenter {

    override fun checkUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null)
            view.showAsUser(user.displayName.toString(), user.email.toString())
        else
            view.showAsGuest()
    }
}