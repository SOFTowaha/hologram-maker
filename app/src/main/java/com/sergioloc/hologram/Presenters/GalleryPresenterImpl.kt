package com.sergioloc.hologram.Presenters

import android.support.v4.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Handler
import com.sergioloc.hologram.Adapter.AdapterImageCloud
import com.sergioloc.hologram.Adapter.AdapterImageLocal
import com.sergioloc.hologram.Interactor.GalleryInteractorImpl
import com.sergioloc.hologram.Interfaces.GalleryInterface

class GalleryPresenterImpl(var view: GalleryInterface.View, var context: Context): GalleryInterface.Presenter {

    private var cloudView = false
    private var interactor = GalleryInteractorImpl(this, context)

    override fun newInstance() {
        interactor.newInstance()
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

    override fun localListUpdated(adapterImageLocal: AdapterImageLocal) {
        view.showLocalListUpdated(adapterImageLocal)
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

    override fun callSaveCloudImage(name: String) {
        view?.showLoading()
        interactor.uploadImageToDatabase(name)
        interactor.uploadImageToStorage(name)
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