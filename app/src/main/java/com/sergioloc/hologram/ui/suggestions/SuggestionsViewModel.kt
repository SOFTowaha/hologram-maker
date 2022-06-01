package com.sergioloc.hologram.ui.suggestions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergioloc.hologram.data.model.Suggestion
import com.sergioloc.hologram.domain.SaveSuggestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuggestionsViewModel @Inject constructor(
    private val saveSuggestionUseCase: SaveSuggestionUseCase
): ViewModel() {

    val response: MutableLiveData<Result<Int>> = MutableLiveData()

    fun saveSuggestion(suggestion: Suggestion) {
        viewModelScope.launch {
            val result = saveSuggestionUseCase(suggestion)
            response.postValue(Result.success(result))
        }
    }

}