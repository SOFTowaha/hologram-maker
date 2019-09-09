package com.sergioloc.hologram.Views

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.app.Fragment
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sergioloc.hologram.Adapter.AdapterImageCloud
import com.sergioloc.hologram.Adapter.AdapterImageLocal
import com.sergioloc.hologram.Dialogs.DialogImageUpload
import com.sergioloc.hologram.Interfaces.GalleryInterface
import com.sergioloc.hologram.Presenters.GalleryPresenterImpl
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.dialog_image_upload.*
import kotlinx.android.synthetic.main.fragment_gallery.*

@SuppressLint("ValidFragment")
class GalleryFragment(var guest: Boolean): Fragment(), GalleryInterface.View {

    private var viewFragment: View? = null
    private var switchType: Switch? = null
    private var tvLocal: TextView? = null
    private var tvCloud: TextView? = null
    private var rvImg: RecyclerView? = null
    private var button: ImageButton? = null
    private var presenter: GalleryPresenterImpl? = null
    private var dialog: DialogImageUpload? = null
    private var loader: ProgressBar? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewFragment =  inflater.inflate(R.layout.fragment_gallery, container, false)


        loadView()
        showLocalView()
        presenter = GalleryPresenterImpl(this, context!!)
        presenter?.newInstance(guest)

        switchType?.setOnCheckedChangeListener { buttonView, isChecked ->
            presenter?.onSwitch(guest)
        }

        button?.setOnClickListener {
            presenter?.callButton(this)
        }


        return viewFragment
    }

    override fun loadView() {
        val activity = activity as AppCompatActivity
        activity.title = "My holograms"
        switchType = viewFragment?.findViewById(R.id.swType)
        tvLocal = viewFragment?.findViewById(R.id.tvLocal)
        tvCloud = viewFragment?.findViewById(R.id.tvCloud)
        rvImg = viewFragment?.findViewById(R.id.rvImages)
        button = viewFragment?.findViewById(R.id.bAddImage)
        loader = viewFragment?.findViewById(R.id.loading)
    }

    override fun showLocalListUpdated(adapterLocal: AdapterImageLocal){
        var gridLayoutManager = GridLayoutManager(context, 3)
        rvImg?.setHasFixedSize(true)
        rvImg?.layoutManager = gridLayoutManager
        rvImg?.adapter = adapterLocal
    }

    override fun showCloudListUpdated(adapterCloud: AdapterImageCloud) {
        var gridLayoutManager = GridLayoutManager(context, 3)
        rvImg?.setHasFixedSize(true)
        rvImg?.layoutManager = gridLayoutManager
        rvImg?.adapter = adapterCloud
        hideLoading()
    }

    override fun showLocalView() {
        switchType?.isChecked = true
        tvLocal?.setTypeface(null, Typeface.BOLD)
        tvCloud?.setTypeface(null, Typeface.NORMAL)
        tvLocal?.setTextColor(resources.getColor(R.color.colorWhite))
        tvCloud?.setTextColor(resources.getColor(R.color.colorGrayT))
    }

    override fun showCloudView() {
        switchType?.isChecked = false
        tvCloud?.setTypeface(null, Typeface.BOLD)
        tvLocal?.setTypeface(null, Typeface.NORMAL)
        tvCloud?.setTextColor(resources.getColor(R.color.colorWhite))
        tvLocal?.setTextColor(resources.getColor(R.color.colorGrayT))
    }

    override fun showGuestError() {
        toast("Debes iniciar sesión para subir fotos a la nube")
        switchType?.isChecked = true
    }

    override fun showConnectionError() {
        toast("Debes tener acceso a internet para subir fotos a la nube")
        switchType?.isChecked = true
    }

    override fun showDialog(bitmap: Bitmap, cloudView: Boolean) {
        dialog = DialogImageUpload(context!!)
        dialog?.show()
        dialog?.ivImageLoaded?.setImageBitmap(bitmap)
        dialog?.bCloseDialog?.setOnClickListener {
            dialog?.dismiss()
        }
        dialog?.bUploadImage?.setOnClickListener {
            if (dialog?.getImageName()!!.isNotEmpty()){
                if (cloudView)
                    presenter?.callSaveCloudImage(dialog?.getImageName()!!)
                else
                    presenter?.callSaveLocalImage(bitmap)
                dialog?.dismiss()
            }
            else{
                toast("Debes escribir un nombre")
            }
        }
    }

    override fun showLoading(){
        loader?.visibility = View.VISIBLE
    }

    override fun hideLoading(){
        loader?.visibility = View.INVISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter?.callActivityResult(requestCode, resultCode, data)
    }

    private fun toast(text: String){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}