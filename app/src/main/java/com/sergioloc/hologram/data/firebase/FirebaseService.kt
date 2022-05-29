package com.sergioloc.hologram.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseService {

    private val db = FirebaseFirestore.getInstance()
    private val getHologramsByName = db.collection(Constants.CATALOG).orderBy("name")


    suspend fun getCatalog(): ArrayList<Hologram> {
        return withContext(Dispatchers.IO) {
            val holograms = ArrayList<Hologram>()
            FirebaseResponse.Success(getHologramsByName.get().await().documents.mapNotNull { response ->
                response.toObject(Hologram::class.java)?.let { hologram ->
                    holograms.add(hologram)
                }
            })
            holograms
        }
    }

}