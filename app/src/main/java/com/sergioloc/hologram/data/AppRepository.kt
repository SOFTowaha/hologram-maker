package com.sergioloc.hologram.data

import com.sergioloc.hologram.data.firebase.FirebaseService
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.utils.Session

class AppRepository {

    private val api = FirebaseService()

    suspend fun getCatalog(): ArrayList<Hologram>? {
        if (Session.catalog != null)
            return Session.catalog

        val response = api.getCatalog()
        Session.catalog = response
        return response
    }

}