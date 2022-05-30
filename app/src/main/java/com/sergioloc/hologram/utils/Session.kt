package com.sergioloc.hologram.utils

import android.graphics.Bitmap
import com.sergioloc.hologram.data.model.Hologram

object Session {
    var newsIds: ArrayList<String>? = null
    var news: ArrayList<Hologram>? = null
    var catalog: ArrayList<Hologram>? = null
    var bitmap: Bitmap? = null
}