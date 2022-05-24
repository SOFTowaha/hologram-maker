package com.sergioloc.hologram.usecases.home

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sergioloc.hologram.models.Hologram

class HomeViewModel: ViewModel() {

    private val _news: MutableLiveData<Result<ArrayList<Hologram>>> = MutableLiveData()
    val news: LiveData<Result<ArrayList<Hologram>>> get() = _news

    fun getNews() {
        val holograms = arrayListOf(
            Hologram("Video 1", "https://docs.microsoft.com/es-es/windows/apps/design/controls/images/image-licorice.jpg", "Tag 1", ""),
            Hologram("Video 2", "https://thumbs.dreamstime.com/b/profil-de-hibou-16485620.jpg", "Tag 2", ""),
            Hologram("Video 3", "https://tinypng.com/images/smart-resizing/new-aspect-ratio.jpg", "Tag 3", ""),
            Hologram("Video 4", "https://www.dcode.fr/tools/image-randomness/images/random-dcode.png", "Tag 4", ""),
            Hologram("Video 5", "https://res.cloudinary.com/demo/image/upload/w_500,f_auto/sample", "Tag 5", ""),
            Hologram("Video 6", "", "Tag 6", ""),
            Hologram("Video 7", "", "Tag 7", ""),
            Hologram("Video 8", "", "Tag 8", ""),
            Hologram("Video 9", "", "Tag 9", ""),
            Hologram("Video 10", "", "Tag10", "")
        )

        Handler(Looper.getMainLooper()).postDelayed({
            _news.postValue(Result.success(holograms))
        }, 3000)
    }



}