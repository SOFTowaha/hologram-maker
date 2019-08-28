package com.sergioloc.hologram.Interfaces

interface NavigateInterface {

    interface View {
        fun showAsGuest()
        fun showAsUser(name: String, email: String)
    }

    interface Presenter {
        fun checkUser()
    }

}