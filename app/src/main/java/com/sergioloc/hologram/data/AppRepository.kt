package com.sergioloc.hologram.data

import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.utils.Session

class AppRepository {

    private val api = FirebaseService()

    suspend fun getNewsIds(): ArrayList<String>? {
        if (Session.newsIds != null)
            return Session.newsIds

        val response = api.getNewsIds()
        Session.newsIds = response
        return response
    }

    suspend fun getHolograms(ids: ArrayList<String>): ArrayList<Hologram>? {
        if (Session.news != null)
            return Session.news

        val holograms = ArrayList<Hologram>()
        for (id in ids) {
            val response = api.getHologram(id)
            response?.let {
                holograms.add(it)
            }
        }
        return holograms
    }

    suspend fun getCatalog(): ArrayList<Hologram>? {
        if (Session.catalog != null)
            return Session.catalog

        val response = api.getCatalog()
        Session.catalog = response
        return response
    }

}