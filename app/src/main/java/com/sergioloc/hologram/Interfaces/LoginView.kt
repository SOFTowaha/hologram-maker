package com.sergioloc.hologram.Interfaces

interface LoginView {
    fun changeToSingIn()
    fun changeToSignUp()
    fun showFieldError(type: Int)
    fun showSignInError()
    fun showSignUpError()
    fun showSignInSuccess()
    fun showSignUpSuccess()
    fun navigateToHome(guest: Boolean)
}