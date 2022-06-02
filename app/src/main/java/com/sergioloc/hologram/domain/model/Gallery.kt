package com.sergioloc.hologram.domain.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.sergioloc.hologram.data.database.entity.GalleryEntity

data class Gallery(
    val id: Int = 0,
    val data: ByteArray,
    val bitmap: Bitmap?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Gallery

        if (id != other.id) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + data.contentHashCode()
        return result
    }
}

fun Gallery.toData() = GalleryEntity(data = data)

fun GalleryEntity.toDomain() = Gallery(id, data, BitmapFactory.decodeByteArray(data, 0, data.size))
