package com.sergioloc.hologram.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @ColumnInfo(name = "id") val id: String = ""
)
