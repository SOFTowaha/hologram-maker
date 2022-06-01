package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.CatalogRepository
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.toCatalogData
import com.sergioloc.hologram.utils.Session
import javax.inject.Inject

class GetCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository
) {

    suspend operator fun invoke(): List<Hologram> {
        if (Session.catalogLoaded)
            return repository.getCatalogFromDatabase()

        var catalog = repository.getCatalogFromFirebase()
        if (catalog.isEmpty())
            catalog = repository.getCatalogFromDatabase()
        else {
            Session.catalogLoaded = true
            repository.deleteCatalogFromDatabase()
            repository.insertCatalogInDatabase(catalog.map { it.toCatalogData() })
        }

        return catalog
    }

}