package com.sergioloc.hologram.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

class Converter {

    companion object {

        fun dpToPx(context: Context, dp: Float): Int {
            val r: Resources = context.resources
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.displayMetrics
            ).toInt()
        }

    }
}