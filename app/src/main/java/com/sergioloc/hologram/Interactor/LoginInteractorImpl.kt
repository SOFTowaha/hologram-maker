package com.sergioloc.hologram.Interactor

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.sergioloc.hologram.Interfaces.LoginInteractor
import com.sergioloc.hologram.Presenters.LoginPresenterImpl

class LoginInteractorImpl(var presenter: LoginPresenterImpl, var mAuth: FirebaseAuth) : LoginInteractor {

    override fun signIn(email: String, password: String) {
        if (email == "" && password == "")
            presenter.fieldError(1)
        else if (email == "" && password != "")
            presenter.fieldError(2)
        else if (email != "" && password == "")
            presenter.fieldError(3)
        else{
            mAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        Log.d("SESION", "signInWithEmail:onComplete:" + task.isSuccessful)
                        if (!task.isSuccessful) {
                            Log.d("SESION", task.exception!!.message)
                            presenter.errorSignIn()
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
        else{
            mAuth!!.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        Log.d("SESION", "createUserWithEmail:onComplete:" + task.isSuccessful)
                        if (!task.isSuccessful) {
                            Log.d("SESION", task.exception!!.message)
                            presenter.errorSignUp()
                        }

                    }
        }
    }
}