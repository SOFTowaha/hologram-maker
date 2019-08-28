package com.sergioloc.hologram.Dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.sergioloc.hologram.R
import com.sergioloc.hologram.Views.ImageActivity

class DialogImageDetail(context: Context) : Dialog(context) {

    private val ivDetail: ImageView
    private val ivPlay: ImageView
    private val ivClose: ImageView
    private var image: Uri? = null


    init {
        setContentView(R.layout.dialog_image_details)

        ivDetail = findViewById(R.id.ivImage)
        ivPlay = findViewById(R.id.ivPlay)
        ivClose = findViewById(R.id.ivCose)

        buttons()
        window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
    }

    fun startDialog(uri: Uri) {
        image = uri
        Glide.with(context)
                .load(uri)
                .into(ivDetail)
        show()
    }

    private fun buttons() {
        ivClose.setOnClickListener { dismiss() }

        ivPlay.setOnClickListener {
            val i = Intent(context, ImageActivity::class.java)
            i.putExtra("imageUri", image!!.toString())
            context.startActivity(i)
            dismiss()
        }
    }
}