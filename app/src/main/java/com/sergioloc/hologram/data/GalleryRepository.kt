package com.sergioloc.hologram.data

import com.sergioloc.hologram.data.database.dao.GalleryDao
import com.sergioloc.hologram.data.database.entity.GalleryEntity
import com.sergioloc.hologram.domain.model.Gallery
import com.sergioloc.hologram.domain.model.toData
import com.sergioloc.hologram.domain.model.toDomain
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val galleryDao: GalleryDao
) {

    suspend fun getGalleryFromDatabase(): List<Gallery> {
        val response = galleryDao.getImages()
        return response.map { it.toDomain() }
    }

    suspend fun insertImageInGallery(image: Gallery) {
        galleryDao.insertImage(image.toData())
    }

    suspend fun deleteImageFromGallery(id: Int) {
        galleryDao.deleteImage(id)
    }

}