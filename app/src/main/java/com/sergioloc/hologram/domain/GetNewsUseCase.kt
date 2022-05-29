package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.AppRepository
import com.sergioloc.hologram.data.model.Hologram

class GetNewsUseCase {

    private val repository = AppRepository()

    suspend operator fun invoke(): ArrayList<Hologram> {
        val ids = repository.getNewsIds()
        val holograms = ArrayList<Hologram>()

        for (id in ids) {
            val hologram = repository.getHologram(id)
            hologram?.let {
                holograms.add(hologram)
            }
        }

        return holograms
    }

}