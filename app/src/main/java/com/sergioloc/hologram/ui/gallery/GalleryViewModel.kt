package com.sergioloc.hologram.ui.gallery

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sergioloc.hologram.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
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
            } catch (e: Exception) { }
            position++
            if (bitmaps.size == size)
                hasNext = false
        }
        _list.postValue(Result.success(bitmaps))
    }

    fun saveNewHologram(context: Context, bitmap: Bitmap, position: Int) {
        try {
            val imageName = "$position.jpg"
            val fos: FileOutputStream = context.openFileOutput(imageName, Context.MODE_PRIVATE)

            var b = bitmap
            if (bitmap.width > Constants.MAX_WIDTH)
                b = getResizedBitmap(b)
            b.compress(Bitmap.CompressFormat.JPEG, 50, fos)
            fos.close()
            imageNames.add("$position.jpg")
            _newImage.postValue(Result.success(b))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun deleteHologram(context: Context, position: Int) {
        val file = File(context.filesDir, imageNames[position])
        if (file.delete()) {
            imageNames.removeAt(position)
            _deleteImage.postValue(Result.success(position))
        }
        else
            _deleteImage.postValue(Result.failure(Throwable()))
    }

    private fun getResizedBitmap(image: Bitmap): Bitmap {
        val ratioBitmap = image.width.toFloat() / image.height.toFloat()

        val finalWidth: Float
        val finalHeight: Float

        if (ratioBitmap < 1)  {
            // Vertical
            finalWidth = Constants.MAX_WIDTH * ratioBitmap
            finalHeight = Constants.MAX_WIDTH
        }
        else {
            // Horizontal
            finalWidth = Constants.MAX_WIDTH
            finalHeight = Constants.MAX_WIDTH / ratioBitmap
        }

        return Bitmap.createScaledBitmap(image, finalWidth.toInt(), finalHeight.toInt(), true)
    }

}