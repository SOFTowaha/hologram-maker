package com.sergioloc.hologram.ui.dialogs

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import com.sergioloc.hologram.databinding.DialogGalleryBinding

class GalleryDialog(context: Context): CustomDialog() {

    private val binding = DialogGalleryBinding.inflate(LayoutInflater.from(context))

    init {
        setView(context, binding.root)

        binding.ivImage.setOnClickListener {
            dismiss()
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        binding.ivImage.setImageBitmap(bitmap)
    }

    fun setOnDeleteClickListener(onClickListener: () -> Unit) {
        binding.btnDelete.setOnClickListener {
            onClickListener()
            dialog.dismiss()
        }
    }

    fun setOnHologramClickListener(onClickListener: () -> Unit) {
        binding.btnCreate.setOnClickListener {
            onClickListener()
            dialog.dismiss()
        }
    }

}