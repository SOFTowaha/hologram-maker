package com.sergioloc.hologram.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sergioloc.hologram.data.database.entity.CatalogEntity

@Dao
interface CatalogDao {

    @Query("SELECT * FROM catalog ORDER BY name DESC")
    suspend fun getCatalog(): List<CatalogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalog(catalog: List<CatalogEntity>)

    @Query("DELETE FROM news")
    suspend fun deleteNews()

}