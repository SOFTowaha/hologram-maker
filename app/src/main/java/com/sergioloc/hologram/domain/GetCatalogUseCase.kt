package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.CatalogRepository
import com.sergioloc.hologram.domain.model.Hologram
import javax.inject.Inject

class GetCatalogUseCase @Inject constructor(
    private val repository: CatalogRepository
) {

    suspend operator fun invoke(): List<Hologram>? = repository.getCatalog()

}