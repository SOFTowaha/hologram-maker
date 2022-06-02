package com.sergioloc.hologram.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.domain.usecase.home.GetNewsUseCase
import com.sergioloc.hologram.domain.model.Hologram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase
): ViewModel() {

    val news: MutableLiveData<Result<List<Hologram>>> = MutableLiveData()

    fun getNews() {
        viewModelScope.launch {
            val result = getNewsUseCase()
            news.postValue(Result.success(result))
        }
    }



}