package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.AppRepository
import com.sergioloc.hologram.data.model.Hologram
import javax.inject.Inject

class FilterCatalogUseCase @Inject constructor(
    private val repository: AppRepository
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