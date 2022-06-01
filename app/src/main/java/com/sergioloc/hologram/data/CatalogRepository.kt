package com.sergioloc.hologram.data

import android.util.Log
import com.sergioloc.hologram.data.database.dao.CatalogDao
import com.sergioloc.hologram.data.database.dao.GalleryDao
import com.sergioloc.hologram.data.database.dao.NewsDao
import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.data.model.HologramModel
import com.sergioloc.hologram.domain.model.Hologram
import com.sergioloc.hologram.domain.model.Suggestion
import com.sergioloc.hologram.domain.model.toData
import com.sergioloc.hologram.domain.model.toDomain
import com.sergioloc.hologram.utils.Connection
import com.sergioloc.hologram.utils.Session
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    private val api: FirebaseService,
    private val catalogDao: CatalogDao
) {

    suspend fun getCatalog(): List<Hologram>? {
        if (Session.catalog != null)
            return Session.catalog

        // TODO: get from local database
        if (!Connection.isInternetAvailable())
            Log.i("AAAAAA", "No internet")

        val response: ArrayList<HologramModel> = api.getCatalog()
        val responseDomain = response.map { it.toDomain() }
        Session.catalog = responseDomain
        return responseDomain
    }

    suspend fun putSuggestion(suggestion: Suggestion): Int {
        return api.putSuggestion(suggestion.toData())
    }

}