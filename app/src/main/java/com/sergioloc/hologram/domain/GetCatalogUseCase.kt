package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.AppRepository
import com.sergioloc.hologram.data.model.Hologram
import javax.inject.Inject

class GetCatalogUseCase @Inject constructor(
    private val repository: AppRepository
) {

    suspend operator fun invoke(): ArrayList<Hologram>? = repository.getCatalog()

}