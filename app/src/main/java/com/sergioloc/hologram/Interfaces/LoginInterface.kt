package com.sergioloc.hologram.Interfaces

interface LoginInterface {

    interface View {
        fun changeToSingIn()
        fun changeToSignUp()
        fun showFieldError(type: Int)
        fun showSignInError()
        fun showSignUpError()
        fun showSignInSuccess()
        fun showSignUpSuccess()
        fun navigateToHome(guest: Boolean)
    }

    interface Presenter {
        fun init()
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String, passwordR: String)
        fun fieldError(type: Int)
        fun errorSignIn()
        fun errorSignUp()
        fun addListener()
        fun removeListener()
    }

    interface Interactor {
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String, passwordR: String)
    }

}