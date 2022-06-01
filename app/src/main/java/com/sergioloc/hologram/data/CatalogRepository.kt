package com.sergioloc.hologram.data

import com.sergioloc.hologram.data.database.dao.CatalogDao
import com.sergioloc.hologram.data.database.entity.CatalogEntity
import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.data.model.HologramModel
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.toDomain
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    private val api: FirebaseService,
    private val catalogDao: CatalogDao
) {

    suspend fun getCatalogFromFirebase(): List<Hologram> {
        val response: List<HologramModel> = api.getCatalog()
        return response.map { it.toDomain() }
    }

    suspend fun getCatalogFromDatabase(): List<Hologram> {
        val response: List<CatalogEntity> = catalogDao.getCatalog()
        return response.map { it.toDomain() }
    }

    suspend fun insertCatalogInDatabase(catalog: List<CatalogEntity>) {
        catalogDao.insertCatalog(catalog)
    }

    suspend fun deleteCatalogFromDatabase() {
        catalogDao.deleteCatalog()
    }

}