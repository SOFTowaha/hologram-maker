package com.sergioloc.hologram.Interfaces

interface NavigateView {
    fun showAsGuest()
    fun showAsUser(name: String, email: String)
}