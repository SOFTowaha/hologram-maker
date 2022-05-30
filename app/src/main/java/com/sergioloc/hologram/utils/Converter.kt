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
                "Animals" -> return ContextCompat.getColor(context, R.color.tag_orange)
                "Movies" -> return ContextCompat.getColor(context, R.color.tag_blue)
                "Space" -> return ContextCompat.getColor(context, R.color.tag_pink)
                "Nature" -> return ContextCompat.getColor(context, R.color.tag_green)
                "Music" -> return ContextCompat.getColor(context, R.color.tag_cyan)
                "Figures" -> return ContextCompat.getColor(context, R.color.tag_yellow)
                "Others" -> return ContextCompat.getColor(context, R.color.tag_purple)
            }
            return ContextCompat.getColor(context, R.color.white)
        }

    }
}