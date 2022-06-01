package com.sergioloc.hologram.ui.gallery

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.domain.model.Gallery
import com.sergioloc.hologram.domain.usecase.gallery.DeleteImageUseCase
import com.sergioloc.hologram.domain.usecase.gallery.GetGalleryUseCase
import com.sergioloc.hologram.domain.usecase.gallery.SaveImageUseCase
import com.sergioloc.hologram.utils.Constants
import com.sergioloc.hologram.utils.extensions.toByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val getGalleryUseCase: GetGalleryUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val deleteImageUseCase: DeleteImageUseCase
): ViewModel() {

    val images: MutableLiveData<Result<List<Gallery>>> = MutableLiveData()
    val newImage: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val deleteImage: MutableLiveData<Result<Boolean>> = MutableLiveData()

    fun getMyImages() {
        viewModelScope.launch {
            val list = getGalleryUseCase()
            images.postValue(Result.success(list))
        }
    }

    fun saveNewImage(bitmap: Bitmap) {
        // Prepare bitmap
        var b = bitmap
        if (bitmap.width > Constants.MAX_WIDTH)
            b = getResizedBitmap(b)

        // Save byte array
        viewModelScope.launch {
            saveImageUseCase(Gallery(data = b.toByteArray(), bitmap = null))
            newImage.postValue(Result.success(true))
        }
    }

    fun deleteImage(image: Gallery) {
        viewModelScope.launch {
            deleteImageUseCase(image)
            deleteImage.postValue(Result.success(true))
        }
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