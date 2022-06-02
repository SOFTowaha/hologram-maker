package com.sergioloc.hologram.domain.usecase.gallery

import com.sergioloc.hologram.data.GalleryRepository
import com.sergioloc.hologram.domain.model.Gallery
import javax.inject.Inject

class GetGalleryUseCase @Inject constructor(
    private val repository: GalleryRepository
) {

    suspend operator fun invoke(): List<Gallery> = repository.getGalleryFromDatabase()

}