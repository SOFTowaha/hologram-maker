package com.sergioloc.hologram.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.sergioloc.hologram.databinding.DialogConfirmationBinding

/** Shows a simple dialog with Yes/No buttons **/

class ConfirmationDialog constructor(val context: Context): CustomDialog() {

    private val binding = DialogConfirmationBinding.inflate(LayoutInflater.from(context))

    /** INIT **/

    init {
        setView(context, binding.root)
        binding.btnNo.setOnClickListener { dialog.dismiss() }
    }

    /** SETTERS **/

    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setMessage(message: String) {
        binding.tvMessage.text = message
    }

    fun setConfirmationButtonText(text: String) {
        binding.btnYes.text = text
    }

    fun setCancelButtonText(text: String) {
        binding.btnNo.text = text
    }

    fun setOnAcceptClickListener(onClickListener: () -> Unit) {
        binding.btnYes.setOnClickListener {
            onClickListener()
            dialog.dismiss()
        }
    }

    fun setOnCancelClickListener(onClickListener: () -> Unit) {
        binding.btnNo.setOnClickListener {
            onClickListener()
            dialog.dismiss()
        }
    }

}