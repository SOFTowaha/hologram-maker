package com.sergioloc.hologram.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.sergioloc.hologram.R

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

        fun pxToDp(context: Context, px: Float): Float {
            return px / context.resources.displayMetrics.density
        }

        fun getTagColor(context: Context, tag: String): Int {
            when (tag) {
                "Animals" -> return ContextCompat.getColor(context, R.color.orange)
                "Movies" -> return ContextCompat.getColor(context, R.color.blue)
                "Space" -> return ContextCompat.getColor(context, R.color.pink)
                "Nature" -> return ContextCompat.getColor(context, R.color.green)
                "Music" -> return ContextCompat.getColor(context, R.color.cyan)
                "Figures" -> return ContextCompat.getColor(context, R.color.yellow)
                "Others" -> return ContextCompat.getColor(context, R.color.purple)
            }
            return ContextCompat.getColor(context, R.color.white)
        }

    }
}