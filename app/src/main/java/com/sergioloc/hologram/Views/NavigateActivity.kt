package com.sergioloc.hologram.Views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.sergioloc.hologram.Interfaces.NavigateInterface
import kotlinx.android.synthetic.main.activity_main.*
import com.sergioloc.hologram.Presenters.NavigatePresenterImpl
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.toolbar_layout.*

/**
 * Created by Sergio López Ceballos on 26/08/2019.
 */

class NavigateActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NavigateInterface.View {


    private var guest: Boolean? = null
    private var presenter: NavigatePresenterImpl? = null
    private var nameH: TextView? = null
    private var emailH: TextView? = null
    private var imageH: ImageView? = null
    private var sesionH: Button? = null

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var lastFragment: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (this as AppCompatActivity).setSupportActionBar(toolbar)

        guest = intent.extras.getBoolean("guest")
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        editor = prefs?.edit()

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        var header = nav_view.getHeaderView(0)

        nameH = header.findViewById(R.id.nameHeader) as TextView
        emailH = header.findViewById(R.id.emailHeader) as TextView
        imageH = header.findViewById(R.id.imageHeader) as ImageView
        sesionH = header.findViewById(R.id.sesionHeader) as Button

        presenter = NavigatePresenterImpl(this)
        presenter?.let {
            it.checkUser()
        }

        lastFragment = prefs!!.getInt("lastFragment", 0)
        when (lastFragment) {
            1 -> supportFragmentManager.beginTransaction().replace(R.id.content_main, GalleryFragment(guest!!)).commit()
            2 -> supportFragmentManager.beginTransaction().replace(R.id.content_main, ListFrag(guest!!)).commit()
            else -> supportFragmentManager.beginTransaction().replace(R.id.content_main, HomeFragment()).commit()
        }



    }

    override fun showAsUser(name: String, email: String) {
        sesionH!!.visibility = View.INVISIBLE
        nameH!!.text = name
        emailH!!.text = email
    }

    override fun showAsGuest() {
        nameH!!.visibility = View.INVISIBLE
        emailH!!.visibility = View.INVISIBLE
        imageH!!.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        var fragment: Fragment? = null

        when (id){
            R.id.nav_home -> fragment = HomeFragment()
            R.id.nav_gallery -> {
                lastFragment = 1
                editor?.putInt("lastFragment", lastFragment!!)
                editor?.apply()
                fragment = GalleryFragment(guest!!)
            }
            R.id.nav_list -> {
                lastFragment = 2
                editor?.putInt("lastFragment", lastFragment!!)
                editor?.apply()
                fragment = ListFrag(guest!!)
            }
            R.id.nav_pyramid -> fragment = PyramidFragment()
            R.id.nav_close -> { fragment = HomeFragment()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.nav_share -> {}
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        supportFragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit()

        return true
    }

}