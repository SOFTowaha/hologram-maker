package com.sergioloc.hologram.Interactor

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.sergioloc.hologram.Interfaces.LoginInterface
import com.sergioloc.hologram.Presenters.LoginPresenterImpl

class LoginInteractorImpl(var presenter: LoginPresenterImpl, var mAuth: FirebaseAuth) : LoginInterface.Interactor {

    override fun signIn(email: String, password: String) {
        if (email == "@gmail.com" && password == "")
            presenter.fieldError(1)
        else if (email == "@gmail.com" && password != "")
            presenter.fieldError(2)
        else if (email != "@gmail.com" && password == "")
            presenter.fieldError(3)
        else{
            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        Log.d("SESION", "signInWithEmail:onComplete:" + task.isSuccessful)
                        if (!task.isSuccessful) {
                            Log.d("SESION", task.exception!!.message)
                            if (task.exception!!.message == "The password is invalid or the user does not have a password.")
                                presenter.errorSignIn(1)
                            else
                                presenter.errorSignIn(0)
                        }
                    }
        }
    }

    override fun signUp(email: String, password: String, passwordR: String) {
        if (email == "" && password == "")
            presenter.fieldError(1)
        else if (email == "" && password != "")
            presenter.fieldError(2)
        else if (email != "" && password == "")
            presenter.fieldError(3)
        else if (password != passwordR)
            presenter.fieldError(4)
        else if (password.length < 6)
            presenter.fieldError(5)
        else{
            mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        Log.d("SESION", "createUserWithEmail:onComplete:" + task.isSuccessful)
                        if (!task.isSuccessful) {
                            Log.d("SESION", task.exception!!.message)
                            if (task.exception!!.message == "The email address is already in use by another account.")
                                presenter.errorSignUp(1)
                            else
                                presenter.errorSignUp(0)
                        }

                    }
        }
    }
}