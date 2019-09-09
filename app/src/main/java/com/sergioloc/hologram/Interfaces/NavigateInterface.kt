package com.sergioloc.hologram.Interfaces

interface NavigateInterface {

    interface View {
        fun showAsGuest()
        fun showAsUser(email: String)
    }

    interface Presenter {
        fun checkUser()
    }

}