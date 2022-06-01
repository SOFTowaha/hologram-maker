package com.sergioloc.hologram.domain.model

import com.sergioloc.hologram.data.database.entity.GalleryEntity

data class Gallery(
    val id: Int,
    val data: String
)

fun Gallery.toData() = GalleryEntity(id, data)

fun GalleryEntity.toDomain() = Gallery(id, data)
