package com.sergioloc.hologram.Views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.sergioloc.hologram.Interfaces.HomeInterface
import com.sergioloc.hologram.Models.VideoModel
import com.sergioloc.hologram.Presenters.HomePresenterImpl
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.fragment_main.*

class HomeFragment: HomeInterface.View, Fragment() {

    private var presenter: HomePresenterImpl? = null
    private var play: ImageView? = null
    private var image1: ImageView? = null
    private var image2: ImageView? = null
    private var image3: ImageView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v = inflater?.inflate(R.layout.fragment_main, container, false)

        presenter = HomePresenterImpl(this, context!!)
        play = v.findViewById(R.id.ivPlay)
        image1 = v.findViewById(R.id.ivNew1)
        image2 = v.findViewById(R.id.ivNew2)
        image3 = v.findViewById(R.id.ivNew3)

        play?.setOnClickListener {
            presenter?.buttonPressed()
        }
        presenter?.initNews()

        image1?.setOnClickListener {
            presenter?.videoPressed(0)
        }

        image2?.setOnClickListener {
            presenter?.videoPressed(1)
        }

        image3?.setOnClickListener {
            presenter?.videoPressed(2)
        }

        return v
    }

    override fun navigateToActivity(code: String) {
        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("id", code)
        startActivity(i)
    }

    override fun showNew(image: Uri, name: String, tag: String, position: Int) {
        when (position){
            0 -> {
                Glide.with(context!!)
                        .load(image)
                        .into(ivNew1)
                tvNew1.text = name
                tvTag1.text = tag
                when (tag){
                    "Animals" -> tvTag1.setTextColor(resources.getColor(R.color.orange))
                    "Films" -> tvTag1.setTextColor(resources.getColor(R.color.blue))
                    "Space" -> tvTag1.setTextColor(resources.getColor(R.color.pink))
                    "Nature" -> tvTag1.setTextColor(resources.getColor(R.color.green))
                    "Music" -> tvTag1.setTextColor(resources.getColor(R.color.cyan))
                    "Figures" -> tvTag1.setTextColor(resources.getColor(R.color.yellow))
                    "Others" -> tvTag1.setTextColor(resources.getColor(R.color.purple))
                }
            }
            1 -> {
                Glide.with(context!!)
                        .load(image)
                        .into(ivNew2)
                tvNew2.text = name
                tvTag2.text = tag
                when (tag){
                    "Animals" -> tvTag2.setTextColor(resources.getColor(R.color.orange))
                    "Films" -> tvTag2.setTextColor(resources.getColor(R.color.blue))
                    "Space" -> tvTag2.setTextColor(resources.getColor(R.color.pink))
                    "Nature" -> tvTag2.setTextColor(resources.getColor(R.color.green))
                    "Music" -> tvTag2.setTextColor(resources.getColor(R.color.cyan))
                    "Figures" -> tvTag2.setTextColor(resources.getColor(R.color.yellow))
                    "Others" -> tvTag2.setTextColor(resources.getColor(R.color.purple))
                }
            }
            2 -> {
                Glide.with(context!!)
                        .load(image)
                        .into(ivNew3)
                tvNew3.text = name
                tvTag3.text = tag
                when (tag){
                    "Animals" -> tvTag3.setTextColor(resources.getColor(R.color.orange))
                    "Films" -> tvTag3.setTextColor(resources.getColor(R.color.blue))
                    "Space" -> tvTag3.setTextColor(resources.getColor(R.color.pink))
                    "Nature" -> tvTag3.setTextColor(resources.getColor(R.color.green))
                    "Music" -> tvTag3.setTextColor(resources.getColor(R.color.cyan))
                    "Figures" -> tvTag3.setTextColor(resources.getColor(R.color.yellow))
                    "Others" -> tvTag3.setTextColor(resources.getColor(R.color.purple))
                }
            }
        }

    }
}