package com.sergioloc.hologram.Presenters

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.sergioloc.hologram.Interactor.LoginInteractorImpl
import com.sergioloc.hologram.Interfaces.LoginInterface

class LoginPresenterImpl(var view: LoginInterface.View) : LoginInterface.Presenter {

    private var mAuth: FirebaseAuth? = null
    private var interactor : LoginInteractorImpl? = null
    private var mAuthListener : FirebaseAuth.AuthStateListener? = null

    override fun init() {
        mAuth = FirebaseAuth.getInstance()
        mAuth?.let {
            interactor = LoginInteractorImpl(this, it)
        }
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d("SESION", "onAuthStateChanged:signed_in:" + user.uid)
                view.navigateToHome(false)
            } else {
                // User is signed out
                Log.d("SESION", "onAuthStateChanged:signed_out")
            }
        }
    }
    override fun signIn(email: String, password: String) {
        interactor?.let {
            it.signIn(email, password)
        }
    }

    override fun signUp(email: String, password: String, passwordR: String) {
        interactor?.let {
            it.signUp(email, password, passwordR)
        }
    }

    override fun fieldError(type: Int) {
        if (view != null){
            view.showFieldError(type)
        }
    }

    override fun errorSignIn() {
        if (view != null)
            view.showSignInError()
    }

    override fun errorSignUp() {
        if (view != null)
            view.showSignUpError()
    }

    override fun addListener() {
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun removeListener() {
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }
}