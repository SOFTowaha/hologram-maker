package com.sergioloc.hologram.ui.login

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.Toast
import com.sergioloc.hologram.R
import com.sergioloc.hologram.ui.navigator.NavigateActivity
import com.sergioloc.hologram.databinding.ActivityAuthBinding
import kotlinx.android.synthetic.main.activity_auth.*
import kotlin.math.hypot

/**
 * Created by Sergio López Ceballos on 25/08/2019.
 */

class LoginActivity : AppCompatActivity(), LoginInterface.View {

    private lateinit var binding: ActivityAuthBinding

    var presenter = LoginPresenterImpl(this)
    var signInActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.init()

        binding.btnLogin.setOnClickListener {
            val fullEmail = email.text.toString() + "@gmail.com"
            presenter.signIn(fullEmail, password.text.toString().trim())
        }

        binding.btnSignUp.setOnClickListener {
            val fullEmail = email2.text.toString() + "@gmail.com"
            presenter.signUp(fullEmail, password2.text.toString().trim(), password2R.text.toString().trim())
        }

        guest.setOnClickListener {
            navigateToHome(true)
        }

        change.setOnClickListener {
            signInActive = if (signInActive){
                changeToSignUp()
                false
            } else{
                changeToSingIn()
                true
            }
        }
    }

    override fun changeToSingIn() {
        titleAuth.text = resources.getString(R.string.sign_in)
        val x = signUpLayout.right
        val y = signUpLayout.bottom
        val startRadius = hypot(signInLayout.width.toDouble(), signInLayout.height.toDouble()).toInt()
        val endRadius = 0
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
    }

    override fun changeToSignUp() {
        titleAuth.text = resources.getString(R.string.sign_up)
        val x = signInLayout.right
        val y = signInLayout.bottom
        val startRadius = 0
        val endRadius = hypot(signInLayout.width.toDouble(), signInLayout.height.toDouble()).toInt()
        signUpLayout.visibility = View.VISIBLE
        val anim = ViewAnimationUtils.createCircularReveal(signUpLayout, x, y, startRadius.toFloat(), endRadius.toFloat())
        anim.start()
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
    }

    override fun showSignUpError(type: Int) {
        if (type == 1)
            Toast.makeText(applicationContext, R.string.repeated_user, Toast.LENGTH_LONG).show()
    }

    override fun showSignInSuccess() {

    }

    override fun showSignUpSuccess() {

    }

    override fun navigateToHome(guest: Boolean) {
        val i = Intent(this, NavigateActivity::class.java)
        i.putExtra("guest", guest)
        startActivity(i)
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