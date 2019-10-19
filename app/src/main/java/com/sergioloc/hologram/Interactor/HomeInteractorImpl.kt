package com.sergioloc.hologram.Interactor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Debug
import android.provider.MediaStore
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.sergioloc.hologram.Interfaces.HomeInterface
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sergioloc.hologram.Models.VideoModel
import com.sergioloc.hologram.Views.PlayerActivity


class HomeInteractorImpl(var presenter: HomeInterface.Presenter, var context: Context): HomeInterface.Interactor {

    private var database: FirebaseDatabase? = null
    private var demoRef: DatabaseReference? = null
    private var newsRef: DatabaseReference? = null
    private var mStorage: StorageReference? = null
    private var newsList: ArrayList<VideoModel>? = null


    override fun getDemoFromFirebase() {
        database = FirebaseDatabase.getInstance()
        demoRef = database?.getReference("demo")
        demoRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue(String::class.java)
                presenter?.videoReady(post!!)
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })

    }

    override fun getNewsFromFirebase() {
        var count = 0
        newsList = ArrayList()
        database = FirebaseDatabase.getInstance()
        newsRef = database?.getReference("news")
        newsRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val video = snapshot.getValue(VideoModel::class.java)
                    getUriFromStorage(video!!, count)
                    newsList?.add(video)
                    count++
                }
            }
            override fun onCancelled(databaseError: DatabaseError) { }
        })
    }

    override fun getUriFromStorage(video: VideoModel, position: Int) {
        var path = "gs://hologram-2.appspot.com/images/catalog/" + video.name + ".png"
        mStorage = FirebaseStorage.getInstance().getReferenceFromUrl(path)
        mStorage?.downloadUrl?.addOnSuccessListener { uri ->
            presenter.sendNew(uri, video.name!!, video.tag!!, position)
        }
    }

    override fun goToYouTube(position: Int) {
        val i = Intent(context, PlayerActivity::class.java)
        i.putExtra("id", newsList?.get(position)?.code)
        context?.startActivity(i)
    }
}