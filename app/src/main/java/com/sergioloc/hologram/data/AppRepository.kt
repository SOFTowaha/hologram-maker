package com.sergioloc.hologram.data

import android.util.Log
import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.data.model.Suggestion
import com.sergioloc.hologram.utils.Connection
import com.sergioloc.hologram.utils.Session

class AppRepository {

    private val api = FirebaseService()

    suspend fun getNewsIds(): ArrayList<String>? {
        if (Session.newsIds != null)
            return Session.newsIds

        // TODO: get from local database
        if (!Connection.isInternetAvailable())
            Log.i("AAAAAA", "No internet")

        val response = api.getNewsIds()
        Session.newsIds = response
        return response
    }

    suspend fun getHolograms(ids: ArrayList<String>): ArrayList<Hologram>? {
        if (Session.news != null)
            return Session.news

        // TODO: get from local database
        if (!Connection.isInternetAvailable())
            Log.i("AAAAAA", "No internet")

        val holograms = ArrayList<Hologram>()
        for (id in ids) {
            val response = api.getHologram(id)
            response?.let {
                holograms.add(it)
            }
        }
        Session.news = holograms
        return holograms
    }

    suspend fun getCatalog(): ArrayList<Hologram>? {
        if (Session.catalog != null)
            return Session.catalog

        // TODO: get from local database
        if (!Connection.isInternetAvailable())
            Log.i("AAAAAA", "No internet")

        val response = api.getCatalog()
        Session.catalog = response
        return response
    }

    suspend fun putSuggestion(suggestion: Suggestion): Int {
        return api.putSuggestion(suggestion)
    }

}