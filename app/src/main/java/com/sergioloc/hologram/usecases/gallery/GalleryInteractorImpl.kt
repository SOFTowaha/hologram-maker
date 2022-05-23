package com.sergioloc.hologram.usecases.gallery

import androidx.fragment.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.MediaStore
import java.util.ArrayList
import androidx.appcompat.app.AppCompatActivity
import com.sergioloc.hologram.adapter.AdapterImageCloud
import com.sergioloc.hologram.adapter.GalleryAdapter

class GalleryInteractorImpl(var presenter: GalleryPresenterImpl, var context: Context): AppCompatActivity(), GalleryInterface.Interactor {

    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var localListSize = 0
    private var cloudListSize = 0
    private var localList: ArrayList<Bitmap>? = null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var galleryAdapter: GalleryAdapter? = null
    private var adapterImageCloud: AdapterImageCloud? = null

    override fun newInstance(guest: Boolean) {
        //Prefrences
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        editor = prefs?.edit()
    }

    override fun loadFromInternalStorage() {
        localListSize = prefs!!.getInt("localListSize", 0)
        localList = ArrayList()
        for (i in 0 until localListSize) {
            //val bitmap = ImageSaver(context).setFileName("$i.png").setDirectoryName("images").load()
            //if (bitmap != null)
              //  localList!!.add(bitmap)
        }
        galleryAdapter = GalleryAdapter(localList!!)
        presenter.localListUpdated(galleryAdapter!!)
    }

    override fun saveToInternalStorage(bitmap: Bitmap) {
        //ImageSaver(context).setFileName("$localListSize.png").setDirectoryName("images").save(bitmap)
        localListSize++
        editor?.putInt("localListSize", localListSize)
        editor?.apply()
        loadFromInternalStorage()
    }

    override fun loadFromFirebase() {

    }

    override fun uploadImageToDatabase() {
        cloudListSize++
        editor?.putInt("cloudListSize", cloudListSize)
        editor?.apply()
    }

    override fun deleteImageFromDatabase(name: String) {

    }

    override fun uploadImageToStorage() {

    }

    override fun deleteImageFromStorage(name: String) {

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