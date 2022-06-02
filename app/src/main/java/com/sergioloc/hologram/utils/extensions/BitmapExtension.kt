package com.sergioloc.hologram.utils.extensions

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val bStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 50, bStream)
    return bStream.toByteArray()
}