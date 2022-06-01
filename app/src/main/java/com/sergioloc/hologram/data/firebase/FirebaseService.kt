package com.sergioloc.hologram.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.sergioloc.hologram.data.model.HologramModel
import com.sergioloc.hologram.data.model.SuggestionModel
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.Safe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseService @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getNews(): ArrayList<HologramModel> {
        return withContext(Dispatchers.IO) {
            val holograms = ArrayList<HologramModel>()
            db.collection(Constants.HOME).document(Constants.NEWS).collection(Constants.HOLOGRAMS).get().await().documents.mapNotNull { response ->
                response.data?.let {
                    holograms.add(
                        HologramModel(
                            id = response.id,
                            name = Safe.getString(it, "name"),
                            image = Safe.getString(it, "image"),
                            tag = Safe.getString(it, "tag"),
                            url = Safe.getString(it, "url")
                        )
                    )
                }
            }
            holograms
        }
    }

    suspend fun getHologram(id: String): HologramModel? {
        return withContext(Dispatchers.IO) {
            var hologram: HologramModel? = null
            db.collection(Constants.CATALOG).document(id).get().await().data?.let { response ->
                hologram = HologramModel(
                    id = id,
                    name = Safe.getString(response, "name"),
                    image = Safe.getString(response, "image"),
                    tag = Safe.getString(response, "tag"),
                    url = Safe.getString(response, "url")
                )
            }
            hologram
        }
    }

    suspend fun getCatalog(): ArrayList<HologramModel> {
        return withContext(Dispatchers.IO) {
            val holograms = ArrayList<HologramModel>()
            db.collection(Constants.CATALOG).orderBy("name").get().await().documents.mapNotNull { response ->
                response.toObject(HologramModel::class.java)?.let { hologram ->
                    holograms.add(hologram)
                }
            }
            holograms
        }
    }

    suspend fun putSuggestion(suggestion: SuggestionModel): Int {
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