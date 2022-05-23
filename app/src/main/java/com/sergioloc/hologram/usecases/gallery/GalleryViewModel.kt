package com.sergioloc.hologram.usecases.gallery

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel: ViewModel() {

    private val _list: MutableLiveData<Result<ArrayList<Bitmap>>> = MutableLiveData()
    val list: LiveData<Result<ArrayList<Bitmap>>> get() = _list

    fun getMyHolograms() {

    }

    fun saveNewHologram() {

    }

}