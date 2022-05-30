package com.sergioloc.hologram.domain

import com.sergioloc.hologram.data.AppRepository
import com.sergioloc.hologram.data.model.Hologram

class GetNewsUseCase {

    private val repository = AppRepository()

    suspend operator fun invoke(): ArrayList<Hologram> {
        val ids = repository.getNewsIds()
        var holograms = ArrayList<Hologram>()

        ids?.let {
            repository.getHolograms(it)?.let { h ->
                holograms = h
            }
        }

        return holograms
    }

}