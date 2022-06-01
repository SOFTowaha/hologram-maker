package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.CatalogRepository
import com.sergioloc.hologram.domain.model.Hologram
import javax.inject.Inject

class FilterCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository
) {

    suspend operator fun invoke(tags: ArrayList<String>): ArrayList<Hologram> {
        val catalog = repository.getCatalog()
        val result = ArrayList<Hologram>()

        catalog?.let {
            for (hologram in it)
                if (tags.contains(hologram.tag))
                    result.add(hologram)
        }

        return result
    }

}