package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.CatalogRepository
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.toCatalogData
import javax.inject.Inject

class GetCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository
) {

    suspend operator fun invoke(): List<Hologram> {
        var catalog = repository.getCatalogFromFirebase()
        if (catalog.isEmpty())
            catalog = repository.getCatalogFromDatabase()
        else {
            repository.deleteCatalogFromDatabase()
            repository.insertCatalogInDatabase(catalog.map { it.toCatalogData() })
        }

        return catalog
    }

}