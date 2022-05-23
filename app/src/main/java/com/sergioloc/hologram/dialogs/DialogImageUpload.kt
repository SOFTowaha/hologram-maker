package com.sergioloc.hologram.dialogs

import android.app.Dialog
import android.content.Context
import com.sergioloc.hologram.R
import kotlinx.android.synthetic.main.dialog_image_upload.*

class DialogImageUpload(context: Context) : Dialog(context) {

    init {
        setContentView(R.layout.dialog_image_upload)
        bCloseDialog.setOnClickListener { dismiss() }
    }
}