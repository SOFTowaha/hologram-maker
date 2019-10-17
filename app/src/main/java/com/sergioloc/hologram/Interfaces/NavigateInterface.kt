package com.sergioloc.hologram.Interfaces

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