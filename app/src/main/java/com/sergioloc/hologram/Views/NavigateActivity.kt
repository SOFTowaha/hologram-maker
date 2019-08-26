package com.sergioloc.hologram.Views

import android.content.Intent
import android.os.Bundle
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
import com.sergioloc.hologram.Fragments.GalleryFragment
import com.sergioloc.hologram.Fragments.HomeFragment
import com.sergioloc.hologram.Fragments.HowFragment
import com.sergioloc.hologram.Fragments.ListFragment
import kotlinx.android.synthetic.main.activity_main.*
import com.sergioloc.hologram.Interfaces.NavigateView
import com.sergioloc.hologram.Presenters.NavigatePresenterImpl
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.toolbar_layout.*

/**
 * Created by Sergio López Ceballos on 26/08/2019.
 */

class NavigateActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NavigateView {


    private var guest: Boolean? = null
    private var presenter: NavigatePresenterImpl? = null
    private var nameH: TextView? = null
    private var emailH: TextView? = null
    private var imageH: ImageView? = null
    private var sesionH: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (this as AppCompatActivity).setSupportActionBar(toolbar)

        guest = intent.extras.getBoolean("guest")

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
            R.id.nav_gallery -> fragment = GalleryFragment(guest)
            R.id.nav_list -> fragment = ListFragment(guest)
            R.id.nav_pyramid -> fragment = HowFragment()
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