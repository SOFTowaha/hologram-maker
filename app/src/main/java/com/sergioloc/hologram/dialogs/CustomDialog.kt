package com.sergioloc.hologram.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.sergioloc.hologram.utils.Converter

/** Parent class for all custom dialogs **/

open class CustomDialog {

    lateinit var dialog: Dialog

    fun setView(context: Context, view: View) {
        dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        (view.layoutParams as FrameLayout.LayoutParams).setMargins(
            Converter.dpToPx(context, 20f),
            0,
            Converter.dpToPx(context, 20f),
            0)
    }

    fun show() {
        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }

}