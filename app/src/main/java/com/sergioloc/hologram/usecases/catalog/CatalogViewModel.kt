package com.sergioloc.hologram.usecases.catalog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.sergioloc.hologram.models.Hologram
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.Safe

class CatalogViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val catalog: MutableLiveData<Result<ArrayList<Hologram>>> = MutableLiveData()

    fun getVideos() {
        db.collection(Constants.CATALOG).orderBy("name").addSnapshotListener { value, error ->
            if (error != null)
                Log.e(Constants.TAG_CATALOG, "Error listening to catalog")

            else {
                val holograms = ArrayList<Hologram>()
                for (i in 0 until value?.documents!!.size) {
                    val hologram = value.documents[i].data
                    hologram?.let {
                        holograms.add(
                            Hologram(
                                name = Safe.getString(hologram, "name"),
                                image = Safe.getString(hologram, "image"),
                                tag = Safe.getString(hologram, "tag"),
                                url = Safe.getString(hologram, "url")
                            )
                        )
                    }
                }
                catalog.postValue(Result.success(holograms))
            }
        }
    }

}