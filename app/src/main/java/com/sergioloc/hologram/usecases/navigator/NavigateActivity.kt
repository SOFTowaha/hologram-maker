package com.sergioloc.hologram.usecases.navigator

import android.content.Intent
import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_main.*
import com.sergioloc.hologram.R
import com.sergioloc.hologram.usecases.catalog.CatalogFragment
import com.sergioloc.hologram.usecases.gallery.GalleryFragment
import com.sergioloc.hologram.usecases.home.HomeFragment
import com.sergioloc.hologram.usecases.pyramid.PyramidFragment
import com.sergioloc.hologram.databinding.ActivityMainBinding
import com.sergioloc.hologram.utils.Constants
import kotlinx.android.synthetic.main.toolbar_layout.*

/**
 * Created by Sergio López Ceballos on 26/08/2019.
 */

class NavigateActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, NavigateInterface.View {


    private lateinit var binding: ActivityMainBinding

    private var guest: Boolean? = null
    private var presenter: NavigatePresenterImpl? = null
    private var emailH: TextView? = null
    private var imageH: ImageView? = null
    private var sesionH: Button? = null
    private var fragmentToNavigate: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (this as AppCompatActivity).setSupportActionBar(toolbar)

        fragmentToNavigate = HomeFragment()
        navigateToFragment()

        initVariables()

        presenter = NavigatePresenterImpl(this)
        presenter?.checkUser()
        presenter?.getShareLink()
    }

    private fun initVariables() {
        // Preferences
        guest = intent.extras?.getBoolean("guest")

        // Header
        val header = binding.navView.getHeaderView(0)
        emailH = header.findViewById(R.id.emailHeader) as TextView
        imageH = header.findViewById(R.id.imageHeader) as ImageView
        sesionH = header.findViewById(R.id.sesionHeader) as Button

        // Navigator
        binding.navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        drawerLayout.addDrawerListener(object:DrawerListener {

            override fun onDrawerClosed(drawerView: View) {
                navigateToFragment()
            }

            override fun onDrawerOpened(drawerView: View) { }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) { }

            override fun onDrawerStateChanged(newState: Int) { }
        })
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

    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> fragmentToNavigate = HomeFragment()
            R.id.nav_gallery -> fragmentToNavigate = GalleryFragment()
            R.id.nav_catalog -> fragmentToNavigate = CatalogFragment()
            R.id.nav_pyramid -> fragmentToNavigate = PyramidFragment()
            R.id.nav_share -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(Intent.EXTRA_TEXT, Constants.PLAY_STORE)
                startActivity(Intent.createChooser(i, resources.getString(R.string.share_usign)))
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun navigateToFragment() {
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentToNavigate?.let { fragmentTransaction.replace(R.id.content_main, it) }
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}