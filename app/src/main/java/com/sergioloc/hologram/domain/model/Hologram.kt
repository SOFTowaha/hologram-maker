package com.sergioloc.hologram.domain.model

import com.sergioloc.hologram.data.database.entity.CatalogEntity
import com.sergioloc.hologram.data.database.entity.NewsEntity
import com.sergioloc.hologram.data.model.HologramModel

data class Hologram(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val tag: String = "",
    val url: String = ""
)

fun Hologram.toData() = NewsEntity(id, name, image, tag, url)

fun HologramModel.toDomain() = Hologram(name, image, tag, url)

fun NewsEntity.toDomain() = Hologram(name, image, tag, url)

fun CatalogEntity.toDomain() = Hologram(name, image, tag, url)
