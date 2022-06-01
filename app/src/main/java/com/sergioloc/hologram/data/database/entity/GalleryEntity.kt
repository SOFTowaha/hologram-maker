package com.sergioloc.hologram.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery")
data class GalleryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: String = "",
    @ColumnInfo(name = "data") val name: String = ""
)
