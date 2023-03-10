package com.sergioloc.hologram.ui.catalog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.domain.usecase.catalog.FilterCatalogUseCase
import com.sergioloc.hologram.domain.usecase.catalog.GetCatalogUseCase
import com.sergioloc.hologram.domain.model.Hologram
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    val getCatalogUseCase: GetCatalogUseCase,
    val filterCatalogUseCase: FilterCatalogUseCase
): ViewModel() {

    val catalog: MutableLiveData<Result<List<Hologram>>> = MutableLiveData()
    private val tags = ArrayList<String>()

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