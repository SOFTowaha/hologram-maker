package com.sergioloc.hologram.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.domain.GetNewsUseCase
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    val news: MutableLiveData<Result<ArrayList<Hologram>>> = MutableLiveData()
    private val getNewsUseCase = GetNewsUseCase()

    fun getNews() {
        viewModelScope.launch {
            val result = getNewsUseCase()
            news.postValue(Result.success(result))
        }
    }



}