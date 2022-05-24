package com.sergioloc.hologram.usecases.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.*

class GalleryViewModel: ViewModel() {

    private val _list: MutableLiveData<Result<ArrayList<Bitmap>>> = MutableLiveData()
    val list: LiveData<Result<ArrayList<Bitmap>>> get() = _list

    private val _newImage: MutableLiveData<Result<Bitmap>> = MutableLiveData()
    val newImage: LiveData<Result<Bitmap>> get() = _newImage

    private val _deleteImage: MutableLiveData<Result<Int>> = MutableLiveData()
    val deleteImage: LiveData<Result<Int>> get() = _deleteImage

    private val imageNames = ArrayList<String>()

    fun getMyHolograms(context: Context, size: Int) {
        var hasNext = true
        var position = 1
        val bitmaps = ArrayList<Bitmap>()
        while (hasNext) {
            try {
                val i: InputStream = context.openFileInput("$position.jpg")
                val b = BitmapFactory.decodeStream(i)
                bitmaps.add(b)
                imageNames.add("$position.jpg")
                position++
            } catch (e: Exception) { }
            if (bitmaps.size == size)
                hasNext = false
        }
        _list.postValue(Result.success(bitmaps))
    }

    fun saveNewHologram(context: Context, bitmap: Bitmap, position: Int) {
        try {
            val imageName = "$position.jpg"
            val fos: FileOutputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos)
            fos.close()
            _newImage.postValue(Result.success(bitmap))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteHologram(context: Context, position: Int) {
        val file = File(context.filesDir, imageNames[position])
        if (file.delete())
            _deleteImage.postValue(Result.success(position))
        else
            _deleteImage.postValue(Result.failure(Throwable()))
    }

}