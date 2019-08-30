package com.sergioloc.hologram.Views

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sergioloc.hologram.Interfaces.HomeInterface
import com.sergioloc.hologram.Presenters.HomePresenterImpl
import com.sergioloc.hologram.R

class HomeFragment: HomeInterface.View, Fragment() {

    private var presenter: HomePresenterImpl? = null
    private var play: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater?.inflate(R.layout.fragment_main, container, false)

        presenter = HomePresenterImpl(this)
        play = v.findViewById(R.id.ivPlay)
        play?.setOnClickListener {
            presenter?.buttonPressed()
        }
        return v
    }

    override fun navigateToActivity(code: String) {
        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("id", code)
        startActivity(i)
    }
}