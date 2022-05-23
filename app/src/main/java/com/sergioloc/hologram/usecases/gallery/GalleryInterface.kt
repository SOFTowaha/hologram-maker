package com.sergioloc.hologram.usecases.gallery

import android.content.Intent
import android.graphics.Bitmap
import androidx.fragment.app.Fragment
import com.sergioloc.hologram.adapter.AdapterImageCloud
import com.sergioloc.hologram.adapter.GalleryAdapter

interface GalleryInterface {

    interface View {
        fun loadView()
        fun showLocalView()
        fun showCloudView()
        fun showGuestError()
        fun showConnectionError()
        fun showLocalListUpdated(galleryAdapter: GalleryAdapter)
        fun showCloudListUpdated(adapterImageLocal: AdapterImageCloud)
        fun showDialog(bitmap: Bitmap, cloudView: Boolean)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun newInstance(guest: Boolean)
        fun onSwitch(guest: Boolean)
        fun localListUpdated(galleryAdapter: GalleryAdapter)
        fun cloudListUpdated(adapterImageCloud: AdapterImageCloud)
        fun callButton(activity: Fragment)
        fun callActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun callDialog(bitmap: Bitmap)
        fun callSaveLocalImage(bitmap: Bitmap)
        fun callSaveCloudImage()
        fun callHideLoading()
    }

    interface Interactor {
        fun newInstance(guest: Boolean)
        fun loadFromInternalStorage()
        fun saveToInternalStorage(bitmap: Bitmap)
        fun uploadImageToDatabase()
        fun uploadImageToStorage()
        fun chooseImageFromGallery(fragment: Fragment)
        fun getImageFromGallery(requestCode: Int, resultCode: Int, data: Intent?)
        fun loadFromFirebase()
        fun deleteImageFromDatabase(name: String)
        fun deleteImageFromStorage(name: String)
    }
}