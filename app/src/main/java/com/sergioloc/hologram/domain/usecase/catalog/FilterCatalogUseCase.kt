package com.sergioloc.hologram.domain.usecase.catalog

import com.sergioloc.hologram.data.CatalogRepository
import com.sergioloc.hologram.domain.model.Hologram
import javax.inject.Inject

class FilterCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository
) {

    suspend operator fun invoke(tags: ArrayList<String>): ArrayList<Hologram> {
        val catalog = repository.getCatalogFromDatabase()
        val result = ArrayList<Hologram>()

        for (hologram in catalog)
            if (tags.contains(hologram.tag))
                result.add(hologram)

        return result
    }

}