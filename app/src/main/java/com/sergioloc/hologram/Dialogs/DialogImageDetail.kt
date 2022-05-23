package com.sergioloc.hologram.Dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.preference.PreferenceManager
import androidx.appcompat.app.AlertDialog
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.sergioloc.hologram.Adapter.AdapterImageLocal
import com.sergioloc.hologram.Interactor.GalleryInteractorImpl
import com.sergioloc.hologram.Interfaces.GalleryInterface
import com.sergioloc.hologram.R
import com.sergioloc.hologram.Views.ImageActivity
import kotlinx.android.synthetic.main.dialog_image_details.*
import java.io.ByteArrayOutputStream




class DialogImageDetail(context: Context, var firebase: Boolean, var interactor: GalleryInterface.Interactor) : Dialog(context) {

    private val ivDetail: ImageView
    private val ivPlay: ImageView
    private val ivClose: ImageView
    private var name: String? = null
    private var position = -1
    private var image: Uri? = null
    private var prefs: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var localListSize = 0


    init {
        setContentView(R.layout.dialog_image_details)
        ivDetail = findViewById(R.id.ivImage)
        ivPlay = findViewById(R.id.ivPlay)
        ivClose = findViewById(R.id.ivCose)
        buttons()
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        editor = prefs?.edit()
        localListSize = prefs!!.getInt("localListSize", 0)
    }

    fun startDialogUri(uri: Uri, n: String, p: Int) {
        name = n
        position = p
        image = uri
        Glide.with(context)
                .load(uri)
                .into(ivDetail)
        show()
    }

    fun startDialogBitmap(bitmap: Bitmap, n: String, p: Int) {
        name = n
        position = p
        ivDetail.setImageBitmap(bitmap)
        show()
    }


    private fun buttons() {
        ivClose.setOnClickListener { dismiss() }

        ivPlay.setOnClickListener {
            val i = Intent(context, ImageActivity::class.java)
            i.putExtra("imagePosition", position)
            image?.let { i.putExtra("imageUri", it.toString()) }
            context.startActivity(i)
            dismiss()
        }

        ivDelete.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.delete)
            builder.setMessage(R.string.sure)
            builder.setNegativeButton(android.R.string.no) { _, _ ->

            }
            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                if (firebase)
                    deleteInFirebase()
                else
                    deleteInStorage()
            }
            builder.show()
            dismiss()
        }
    }

    private fun deleteInFirebase(){
        interactor?.deleteImageFromDatabase(name!!)
        interactor?.deleteImageFromStorage(name!!)
    }

    private fun deleteInStorage(){
        //ImageSaver(context).setFileName("$position.png").setDirectoryName("images").delete()
        Toast.makeText(context, R.string.image_delete, Toast.LENGTH_LONG).show()
        interactor?.loadFromInternalStorage()
    }
}