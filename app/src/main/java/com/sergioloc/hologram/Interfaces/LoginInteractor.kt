package com.sergioloc.hologram.Interfaces

interface LoginInteractor {
    fun signIn(email: String, password: String)
    fun signUp(email: String, password: String, passwordR: String)
}