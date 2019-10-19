package com.sergioloc.hologram.Interactor

import android.support.v4.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.MediaStore
import com.sergioloc.hologram.Presenters.GalleryPresenterImpl
import com.sergioloc.hologram.Utils.ImageSaver
import java.util.ArrayList
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sergioloc.hologram.Adapter.AdapterImageCloud
import com.sergioloc.hologram.Adapter.AdapterImageLocal
import com.sergioloc.hologram.Interfaces.GalleryInterface
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.activity_auth.*


class GalleryInteractorImpl(var presenter: GalleryPresenterImpl, var context: Context): AppCompatActivity(), GalleryInterface.Interactor {

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var localListSize = 0
    private var cloudListSize = 0
    private var localList: ArrayList<Bitmap>? = null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var adapterImageLocal: AdapterImageLocal? = null
    private var adapterImageCloud: AdapterImageCloud? = null

    //Firebase
    private var database: FirebaseDatabase? = null
    private var user: FirebaseUser? = null
    private var images: DatabaseReference? = null
    private var mStorage: StorageReference? = null


    override fun newInstance(guest: Boolean) {
        //Prefrences
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        editor = prefs?.edit()
        if (!guest){ //Firebase
            database = FirebaseDatabase.getInstance()
            user = FirebaseAuth.getInstance().currentUser
            images = database?.getReference("users")?.child(user?.uid)?.child("images")
            mStorage = FirebaseStorage.getInstance().getReference("images")
        }
    }

    override fun loadFromInternalStorage() {
        localListSize = prefs!!.getInt("localListSize", 0)
        localList = ArrayList()
        for (i in 0 until localListSize) {
            val bitmap = ImageSaver(context).setFileName("$i.png").setDirectoryName("images").load()
            if (bitmap != null)
                localList!!.add(bitmap)
        }
        adapterImageLocal = AdapterImageLocal(localList!!, context, this)
        presenter.localListUpdated(adapterImageLocal!!)
    }

    override fun saveToInternalStorage(bitmap: Bitmap) {
        ImageSaver(context)
                .setFileName("$localListSize.png")
                .setDirectoryName("images").save(bitmap)
        localListSize++
        editor?.putInt("localListSize", localListSize)
        editor?.apply()
        loadFromInternalStorage()
    }

    override fun loadFromFirebase() {
        cloudListSize = prefs!!.getInt("cloudListSize", 0)
        var inter = this
        images?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listImages = ArrayList<String>()
                for (snapshot in dataSnapshot.children) {
                    listImages.add(snapshot.value!!.toString())
                }
                adapterImageCloud = AdapterImageCloud(listImages,user!!,mStorage!!,context, inter)
                presenter.cloudListUpdated(adapterImageCloud!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    override fun uploadImageToDatabase() {
        cloudListSize++
        editor?.putInt("cloudListSize", cloudListSize)
        editor?.apply()
        images?.child(cloudListSize.toString())?.setValue(cloudListSize)
    }

    override fun deleteImageFromDatabase(name: String) {
        images?.child(name)?.removeValue()
    }

    override fun uploadImageToStorage() {
        if (filePath != null) {
            val ref = mStorage?.child("users/" + user?.uid + "/" + cloudListSize)
            ref?.putFile(filePath!!)
                    ?.addOnSuccessListener {
                        Toast.makeText(context, R.string.uploaded, Toast.LENGTH_SHORT).show()
                        presenter?.callHideLoading()
                    }
                    ?.addOnFailureListener { e -> Toast.makeText(context, "Failed " + e.message, Toast.LENGTH_SHORT).show() }
        }
    }

    override fun deleteImageFromStorage(name: String) {
        if (filePath != null){
            val ref = mStorage?.child("users/" + user?.uid + "/" + name)
            ref?.delete()
        }
    }

    override fun chooseImageFromGallery(fragment: Fragment) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        fragment.startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun getImageFromGallery(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.data != null) {
            filePath = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, filePath)
            presenter.callDialog(bitmap)
        }
    }
}