package com.sergioloc.hologram.domain.usecase.gallery

import com.sergioloc.hologram.data.GalleryRepository
import com.sergioloc.hologram.domain.model.Gallery
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(image: Gallery) {
        repository.deleteImageFromGallery(image)
    }

}