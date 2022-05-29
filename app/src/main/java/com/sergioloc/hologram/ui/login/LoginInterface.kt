package com.sergioloc.hologram.ui.login

interface LoginInterface {

    interface View {
        fun changeToSingIn()
        fun changeToSignUp()
        fun showFieldError(type: Int)
        fun showSignInError(type: Int)
        fun showSignUpError(type: Int)
        fun showSignInSuccess()
        fun showSignUpSuccess()
        fun navigateToHome(guest: Boolean)
    }

    interface Presenter {
        fun init()
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String, passwordR: String)
        fun fieldError(type: Int)
        fun errorSignIn(type: Int)
        fun errorSignUp(type: Int)
        fun addListener()
        fun removeListener()
    }

    interface Interactor {
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String, passwordR: String)
    }

}