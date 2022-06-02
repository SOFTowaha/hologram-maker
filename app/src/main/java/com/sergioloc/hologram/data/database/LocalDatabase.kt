package com.sergioloc.hologram.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sergioloc.hologram.data.database.dao.CatalogDao
import com.sergioloc.hologram.data.database.dao.GalleryDao
import com.sergioloc.hologram.data.database.dao.NewsDao
import com.sergioloc.hologram.data.database.entity.CatalogEntity
import com.sergioloc.hologram.data.database.entity.GalleryEntity
import com.sergioloc.hologram.data.database.entity.NewsEntity

@Database(entities = [NewsEntity::class, CatalogEntity::class, GalleryEntity::class], version = 1)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun getNewsDao(): NewsDao

    abstract fun getCatalogDao(): CatalogDao

    abstract fun getGalleryDao(): GalleryDao

}