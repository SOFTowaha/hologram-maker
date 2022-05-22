package com.sergioloc.hologram.Views

import android.animation.Animator
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import com.sergioloc.hologram.Interfaces.LoginInterface
import com.sergioloc.hologram.Presenters.LoginPresenterImpl
import com.sergioloc.hologram.R

import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_player.*


/**
 * Created by Sergio López Ceballos on 25/08/2019.
 */

class LoginActivity : AppCompatActivity(), LoginInterface.View {

    var presenter = LoginPresenterImpl(this)
    var signInActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        enter.setBackgroundColor(resources.getColor(R.color.turquoise))
        register.setBackgroundColor(resources.getColor(R.color.turquoise))

        presenter.init()

        enter.setOnClickListener {
            enter.startAnimation()
            var fullEmail = email.text.toString() + "@gmail.com"
            presenter.signIn(fullEmail, password.text.toString().trim())
        }

        register.setOnClickListener {
            register.startAnimation()
            var fullEmail = email2.text.toString() + "@gmail.com"
            presenter.signUp(fullEmail, password2.text.toString().trim(), password2R.text.toString().trim())
        }

        guest.setOnClickListener {
            navigateToHome(true)
        }

        change.setOnClickListener {
            if (signInActive){
                changeToSignUp()
                signInActive = false
            }
            else{
                changeToSingIn()
                signInActive = true
            }
        }
    }

    override fun changeToSingIn() {
        titleAuth.text = resources.getString(R.string.sign_in)
        val x = signUpLayout.right
        val y = signUpLayout.bottom
        val startRadius = Math.hypot(signInLayout.width.toDouble(), signInLayout.height.toDouble()).toInt()
        val endRadius = 0
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val anim = ViewAnimationUtils.createCircularReveal(signUpLayout, x, y, startRadius.toFloat(), endRadius.toFloat())
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {

                }

                override fun onAnimationEnd(animator: Animator) {
                    signUpLayout.visibility = View.GONE
                }

                override fun onAnimationCancel(animator: Animator) {

                }

                override fun onAnimationRepeat(animator: Animator) {

                }
            })
            anim.start()
        } else {
            signUpLayout.visibility = View.GONE
        }
    }

    override fun changeToSignUp() {
        titleAuth.text = resources.getString(R.string.sign_up)
        val x = signInLayout.right
        val y = signInLayout.bottom
        val startRadius = 0
        val endRadius = Math.hypot(signInLayout.width.toDouble(), signInLayout.height.toDouble()).toInt()
        signUpLayout.visibility = View.VISIBLE
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val anim = ViewAnimationUtils.createCircularReveal(signUpLayout, x, y, startRadius.toFloat(), endRadius.toFloat())
            anim.start()
        }
    }

    override fun showFieldError(type: Int) {
        if (signInActive)
            showSignInError(0)
        else
            showSignUpError(0)

        var message = ""
        when(type){
            1 -> message = resources.getString(R.string.error_fields)
            2 -> message = resources.getString(R.string.error_mail)
            3 -> message = resources.getString(R.string.error_password)
            4 -> message = resources.getString(R.string.error_same)
            5 -> message = resources.getString(R.string.error_pass_min)
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSignInError(type: Int) {
        if (type == 1)
            Toast.makeText(applicationContext, R.string.wrong_pass, Toast.LENGTH_LONG).show()
        failureAuth(1)
    }

    override fun showSignUpError(type: Int) {
        if (type == 1)
            Toast.makeText(applicationContext, R.string.repeated_user, Toast.LENGTH_LONG).show()
        failureAuth(2)
    }

    override fun showSignInSuccess() {
        successAuth(1)
    }

    override fun showSignUpSuccess() {
        successAuth(2)
    }

    override fun navigateToHome(guest: Boolean) {
        val i = Intent(this, NavigateActivity::class.java)
        i.putExtra("guest", guest)
        startActivity(i)
    }

    private fun failureAuth(op: Int){
        val icon2 = BitmapFactory.decodeResource(resources, R.drawable.ic_action_error)
        val handler = Handler()
        if (op == 1){
            // Sign In
            enter.doneLoadingAnimation(resources.getColor(R.color.turquoise), icon2)
            handler.postDelayed({ enter.revertAnimation() }, 2000)
        }
        else{
            // Sign Up
            register.doneLoadingAnimation(resources.getColor(R.color.turquoise), icon2)
            handler.postDelayed({ register.revertAnimation() }, 2000)
        }
    }

    private fun successAuth(op: Int){
        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_action_done)
        if (op == 1)
            enter.doneLoadingAnimation(resources.getColor(R.color.turquoise), icon)
        else
            register.doneLoadingAnimation(resources.getColor(R.color.turquoise), icon)
    }

    public override fun onStart() {
        super.onStart()
        presenter.addListener()
    }

    public override fun onStop() {
        super.onStop()
        presenter.removeListener()
    }

}