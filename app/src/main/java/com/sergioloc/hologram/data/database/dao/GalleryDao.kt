package com.sergioloc.hologram.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sergioloc.hologram.data.database.entity.GalleryEntity

@Dao
interface GalleryDao {

    @Query("SELECT * FROM gallery")
    suspend fun getImages(): List<GalleryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<GalleryEntity>)

}