package com.sergioloc.hologram.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.data.model.Suggestion
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.Safe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getNewsIds(): ArrayList<String> {
        return withContext(Dispatchers.IO) {
            var ids = ArrayList<String>()
            db.collection(Constants.HOME).document(Constants.NEWS).get().await().data?.let { response ->
                ids = response["list"] as ArrayList<String>
            }
            ids
        }
    }

    suspend fun getHologram(id: String): Hologram? {
        return withContext(Dispatchers.IO) {
            var hologram: Hologram? = null
            db.collection(Constants.CATALOG).document(id).get().await().data?.let { response ->
                hologram = Hologram(
                    name = Safe.getString(response, "name"),
                    image = Safe.getString(response, "image"),
                    tag = Safe.getString(response, "tag"),
                    url = Safe.getString(response, "url")
                )
            }
            hologram
        }
    }

    suspend fun getCatalog(): ArrayList<Hologram> {
        return withContext(Dispatchers.IO) {
            val holograms = ArrayList<Hologram>()
            db.collection(Constants.CATALOG).orderBy("name").get().await().documents.mapNotNull { response ->
                response.toObject(Hologram::class.java)?.let { hologram ->
                    holograms.add(hologram)
                }
            }
            holograms
        }
    }

    suspend fun putSuggestion(suggestion: Suggestion): Int {
        return withContext(Dispatchers.IO) {
            try {
                db.collection(Constants.SUGGESTIONS).document().set(suggestion).await()
                Constants.SUCCESS
            } catch (e: Exception) {
                Constants.ERROR
            }
        }
    }

}