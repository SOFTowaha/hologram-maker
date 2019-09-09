package com.sergioloc.hologram.Interfaces

import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import com.sergioloc.hologram.Adapter.AdapterImageCloud
import com.sergioloc.hologram.Adapter.AdapterImageLocal

interface GalleryInterface {

    interface View {
        fun loadView()
        fun showLocalView()
        fun showCloudView()
        fun showGuestError()
        fun showConnectionError()
        fun showLocalListUpdated(adapterImageLocal: AdapterImageLocal)
        fun showCloudListUpdated(adapterImageLocal: AdapterImageCloud)
        fun showDialog(bitmap: Bitmap, cloudView: Boolean)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun newInstance(guest: Boolean)
        fun onSwitch(guest: Boolean)
        fun localListUpdated(adapterImageLocal: AdapterImageLocal)
        fun cloudListUpdated(adapterImageCloud: AdapterImageCloud)
        fun callButton(activity: Fragment)
        fun callActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun callDialog(bitmap: Bitmap)
        fun callSaveLocalImage(bitmap: Bitmap)
        fun callSaveCloudImage(name: String)
        fun callHideLoading()
    }

    interface Interactor {
        fun newInstance(guest: Boolean)
        fun loadFromInternalStorage()
        fun saveToInternalStorage(bitmap: Bitmap)
        fun uploadImageToDatabase(name: String)
        fun uploadImageToStorage(name: String)
        fun chooseImageFromGallery(fragment: Fragment)
        fun getImageFromGallery(requestCode: Int, resultCode: Int, data: Intent?)
        fun loadFromFirebase()
        fun deleteImageFromDatabase(name: String)
        fun deleteImageFromStorage(name: String)
    }
}