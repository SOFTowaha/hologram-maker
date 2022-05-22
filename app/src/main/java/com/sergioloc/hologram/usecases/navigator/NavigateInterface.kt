package com.sergioloc.hologram.usecases.navigator

interface NavigateInterface {

    interface View {
        fun showAsGuest()
        fun showAsUser(email: String)
        fun updateShareLink(link: String)
    }

    interface Presenter {
        fun checkUser()
        fun getShareLink()
        fun sendShareLink(link: String)
    }

    interface Interactor {
        fun shareLink()
    }

}