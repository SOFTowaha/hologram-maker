package com.sergioloc.hologram.Views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
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
    private var emailH: TextView? = null
    private var imageH: ImageView? = null
    private var sesionH: Button? = null

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var lastFragment: Int? = null
    private var fragmentToNavigate: Fragment? = null
    private var shareLink = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (this as AppCompatActivity).setSupportActionBar(toolbar)

        guest = intent.extras?.getBoolean("guest")
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        editor = prefs?.edit()

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        var header = nav_view.getHeaderView(0)

        emailH = header.findViewById(R.id.emailHeader) as TextView
        imageH = header.findViewById(R.id.imageHeader) as ImageView
        sesionH = header.findViewById(R.id.sesionHeader) as Button

        presenter = NavigatePresenterImpl(this)
        presenter?.let {
            it.checkUser()
        }

        lastFragment = prefs!!.getInt("lastFragment", 0)
        when (lastFragment) {
            0 -> supportFragmentManager.beginTransaction().replace(R.id.content_main, HomeFragment()).commit()
            1 -> supportFragmentManager.beginTransaction().replace(R.id.content_main, GalleryFragment(guest!!)).commit()
            2 -> supportFragmentManager.beginTransaction().replace(R.id.content_main, CatalogFragment(guest!!)).commit()
            else -> supportFragmentManager.beginTransaction().replace(R.id.content_main, HomeFragment()).commit()
        }

        drawerLayout.addDrawerListener(object:DrawerListener{

            override fun onDrawerClosed(drawerView: View) {
                navigateToFragment()
            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })

        presenter?.let {
            it.getShareLink()
        }

    }

    override fun showAsUser(email: String) {
        sesionH!!.visibility = View.INVISIBLE
        emailH!!.text = email.split("@")[0]
    }

    override fun showAsGuest() {
        emailH!!.visibility = View.INVISIBLE
        imageH!!.visibility = View.INVISIBLE
    }

    override fun updateShareLink(link: String) {
        shareLink = link
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.nav_home -> {
                lastFragment = 0
                editor?.putInt("lastFragment", lastFragment!!)
                editor?.apply()
                fragmentToNavigate = HomeFragment()
            }
            R.id.nav_gallery -> {
                lastFragment = 1
                editor?.putInt("lastFragment", lastFragment!!)
                editor?.apply()
                fragmentToNavigate = GalleryFragment(guest!!)
            }
            R.id.nav_list -> {
                lastFragment = 2
                editor?.putInt("lastFragment", lastFragment!!)
                editor?.apply()
                fragmentToNavigate = CatalogFragment(guest!!)
            }
            R.id.nav_pyramid -> fragmentToNavigate = PyramidFragment()
            R.id.nav_close -> { fragmentToNavigate = HomeFragment()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.nav_share -> {
                var i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, shareLink)
                startActivity(Intent.createChooser(i, resources.getString(R.string.share_usign)))
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun navigateToFragment(){
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentToNavigate?.let { fragmentTransaction.replace(R.id.content_main, it) }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }catch (e: Exception){}
    }

}