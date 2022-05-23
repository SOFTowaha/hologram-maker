package com.sergioloc.hologram.usecases.gallery

import androidx.fragment.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Handler
import com.sergioloc.hologram.adapter.AdapterImageCloud
import com.sergioloc.hologram.adapter.GalleryAdapter

class GalleryPresenterImpl(var view: GalleryInterface.View, var context: Context): GalleryInterface.Presenter {

    private var cloudView = false
    private var interactor = GalleryInteractorImpl(this, context)

    override fun newInstance(guest: Boolean) {
        interactor.newInstance(guest)
        interactor.loadFromInternalStorage()
    }

    override fun onSwitch(guest: Boolean) {
        if (guest){
            view.showGuestError()
        }
        else if (!isInternetConnection()){
            view.showConnectionError()
        }
        else{
            if (cloudView){
                interactor.loadFromInternalStorage()
                view.showLocalView()
            }
            else{
                view.showLoading()
                interactor.loadFromFirebase()
                view.showCloudView()
            }
            cloudView = !cloudView
        }
    }

    override fun localListUpdated(galleryAdapter: GalleryAdapter) {
        view.showLocalListUpdated(galleryAdapter)
    }

    override fun callButton(activity: Fragment) {
        interactor.chooseImageFromGallery(activity)
    }

    override fun callActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        interactor.getImageFromGallery(requestCode, resultCode, data)
    }

    override fun callSaveLocalImage(bitmap: Bitmap) {
        interactor.saveToInternalStorage(bitmap)
    }

    override fun callSaveCloudImage() {
        view?.showLoading()
        interactor.uploadImageToDatabase()
        interactor.uploadImageToStorage()
        Handler().postDelayed({
            interactor.loadFromFirebase()
        }, 4000)
    }

    override fun callHideLoading() {
        view?.hideLoading()
    }

    override fun callDialog(bitmap: Bitmap){
        view.showDialog(bitmap, cloudView)
    }

    override fun cloudListUpdated(adapterImageCloud: AdapterImageCloud) {
        view.showCloudListUpdated(adapterImageCloud)
    }

    private fun isInternetConnection(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}