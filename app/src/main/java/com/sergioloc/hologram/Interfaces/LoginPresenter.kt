package com.sergioloc.hologram.Interfaces

interface LoginPresenter {
    fun init()
    fun signIn(email: String, password: String)
    fun signUp(email: String, password: String, passwordR: String)
    fun fieldError(type: Int)
    fun errorSignIn()
    fun errorSignUp()
    fun addListener()
    fun removeListener()
}