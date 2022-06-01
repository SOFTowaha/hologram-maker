package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.GalleryRepository
import com.sergioloc.hologram.domain.model.Gallery
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(image: Gallery) {
        repository.insertImageInGallery(image)
    }

}