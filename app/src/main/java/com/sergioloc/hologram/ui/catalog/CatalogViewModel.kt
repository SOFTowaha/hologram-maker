package com.sergioloc.hologram.ui.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.data.model.Hologram
import com.sergioloc.hologram.domain.FilterCatalogUseCase
import com.sergioloc.hologram.domain.GetCatalogUseCase
import kotlinx.coroutines.launch

class CatalogViewModel: ViewModel() {

    val catalog: MutableLiveData<Result<ArrayList<Hologram>>> = MutableLiveData()
    private val tags = ArrayList<String>()

    val filterCatalogUseCase = FilterCatalogUseCase()
    val getCatalogUseCase = GetCatalogUseCase()

    fun getCatalog() {
        viewModelScope.launch {
            val result = getCatalogUseCase()
            result?.let {
                catalog.postValue(Result.success(it))
            }
        }
    }

    fun getFilteredCatalog(tag: String, add: Boolean) {
        viewModelScope.launch {
            if (add)
                tags.add(tag)
            else
                tags.remove(tag)
            val result = filterCatalogUseCase(tags)
            catalog.postValue(Result.success(result))
        }
    }


}